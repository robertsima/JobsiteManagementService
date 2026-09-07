# Spec: Entity + Schema Build-Out

## Goal
Finish the boilerplate JPA entity layer so it covers the full README scope (RBAC users,
provider inventory search, quotes -> orders -> payments, volume/discount pricing), then
regenerate the Liquibase `create-schema.sql` and `seed-data.sql` to match.

## Scope
In scope
- `model/entity/*` - full field sets, JPA relationships, Lombok wiring, interface impls.
- `abstraction/*` - only the minimal signature fixes needed for the entities to be typed correctly.
- `abstraction/enums/*` - new enums required by the new fields.
- `src/main/resources/db/changelog/scripts/create-schema.sql` - full rewrite to match entities.
- `src/main/resources/db/changelog/scripts/seed-data.sql` - full rewrite to seed every table.
- `repository/*` - one Spring Data JPA repository per entity/table, typed and with the
  lookup methods each aggregate needs. `OrderRepository`/`QuoteRepository` become interfaces;
  the raw `JpaRepository` on `UserRepository`/`JobsiteRepository` gets its type parameters.

Out of scope (left untouched)
- DTOs / records (`model/dto/*`) - currently mid-edit by the author.
- Services, controllers, orchestration, security config.

## Domain model

| Entity | Table | Purpose |
|---|---|---|
| Business | `businesses` | Composite root - the construction company using the app |
| User | `users` | Person in a business; `UserType` drives RBAC |
| Jobsite | `jobsites` | Physical site owned by a business, requested by a user |
| Provider | `providers` | Supplier (e.g. Home Depot) - prototype/template target |
| Inventory | `inventories` | 1:1 with a provider; holds its items |
| Item | `items` | A SKU inside one provider's inventory |
| VolumeDiscountHierarchy | `volume_discount_hierarchies` | Singleton per provider |
| VolumeDiscountTier | `volume_discount_tiers` | One level of a hierarchy (qty -> % off) |
| Discount | `discounts` | Decorator applied to a price; provider- or item-scoped |
| Quote | `quotes` | Request draft against one provider |
| QuoteLineItem | `quote_line_items` | Priced line on a quote |
| Order | `orders` | Submitted request, optionally converted from a quote |
| OrderLineItem | `order_line_items` | Priced line on an order |
| Payment | `payments` | Idempotent payment against an order |

Relationships
- Business 1..* User, 1..* Jobsite; Business *..* Provider via `business_providers`.
- Provider 1..1 Inventory, 1..1 VolumeDiscountHierarchy, 1..* Discount.
- Inventory 1..* Item.
- VolumeDiscountHierarchy 1..* VolumeDiscountTier.
- Quote/Order -> Business, Jobsite, Provider, User (requestor); Order -> Quote (nullable).
- Quote 1..* QuoteLineItem, Order 1..* OrderLineItem, Order 1..* Payment.

## Interface changes (minimum needed)
| Interface | Before | After | Why |
|---|---|---|---|
| `User.getUserType()` | `String` | `UserType` | Field is the enum; String return collides with the Lombok getter |
| `Item.getPrice()` | `double` | `BigDecimal` | Money must not be a float |
| `Item.getDescripton()` | typo | `getDescription()` | Matches the `description` field |
| `Payment.getPaymentAmount()` | `Double` | `BigDecimal` | Money |
| `Payment.getPaymentDate()` | `Double` | `OffsetDateTime` | A date is not a Double |
| `Discount.calculateDiscount()` / `applyDiscount()` | no args | take `BigDecimal basePrice` | A decorator needs the value it decorates |

`Request.getStatus()` stays `String`: Quote and Order carry different status enums, so the
entities keep a typed field (`getQuoteStatus()` / `getOrderStatus()`) and expose `getStatus()`
as the polymorphic string view.

New enums: `DiscountType` (PERCENTAGE, FIXED_AMOUNT), `UnitOfMeasure`.

## Conventions
- `@Id` is `Long` / `BIGSERIAL`, setter suppressed.
- `@EqualsAndHashCode(of = "id")` and `@ToString` excluding relations - avoids JPA cycles.
- `@NoArgsConstructor` on every entity (JPA requirement once other ctors appear).
- Money: `BigDecimal` / `NUMERIC(19,4)`; currency as `CHAR(3)` defaulting to `USD`.
- Timestamps: `OffsetDateTime` / `TIMESTAMPTZ`, `created_at` defaults to `CURRENT_TIMESTAMP`.
- Collections returned from the abstraction interfaces are unmodifiable copies (encapsulation).
- Natural keys get UNIQUE constraints so the seed can use `ON CONFLICT DO NOTHING`.

## Steps
1. Add `DiscountType` and `UnitOfMeasure` enums.
2. Apply the interface signature fixes above.
3. Rewrite the 11 existing entities; add `VolumeDiscountTier`, `QuoteLineItem`, `OrderLineItem`.
4. Rewrite `create-schema.sql` in FK-safe order.
5. Rewrite `seed-data.sql`: explicit ids, `ON CONFLICT DO NOTHING`, sequence resync at the end.
6. Add a `JpaRepository<Entity, Long>` per table (14 repositories), replacing the raw/class stubs.
   `JobsiteRepository.getLocationsByUserId` is repaired at the same time - its current JPQL
   selects `FROM jobsites` (a table name, not the entity) and would fail at bootstrap.
7. `mvnw compile` to verify.

## Execution notes
Two things surfaced while building this that were not in the original plan:

1. **Lombok was not running at all.** Every `@Getter`/`@Setter` was a no-op because JDK 23+
   stopped discovering annotation processors from the classpath. The existing entities did not
   notice - they declared every method by hand - but nothing that relies on a generated accessor
   would compile. Fixed by declaring Lombok in `maven-compiler-plugin`
   `<annotationProcessorPaths>` in `pom.xml`.
2. **`currency` was `CHAR(3)` in the first draft of the schema.** Hibernate `ddl-auto=validate`
   rejected it against the entity `@Column(length = 3)` on a `String`. Changed to `VARCHAR(3)`,
   which is also the right type - `CHAR` space-pads.

`ItemRepository.search` casts its optional parameters (`CAST(:category AS String)`) because
Postgres cannot infer a type for a null bind parameter and falls back to `bytea`.

## Verification performed
- `mvnw clean test` - BUILD SUCCESS; `InformationManagementControllerIT` still passes.
- Schema and seed executed against Postgres 16 through Liquibase itself (both changesets ran
  clean, so `splitStatements`/`stripComments` handle the new SQL).
- Hibernate `ddl-auto=validate` passed against the generated schema, so all 14 entities match
  their tables.
- A throwaway test bootstrapped all 14 repositories against the seeded data, which parses every
  derived query name and every `@Query` string, then asserted the expected rows come back.
  The test was deleted afterwards.
- Seeded money checked in SQL: every quote and order header equals the sum of its line items,
  every `line_total` equals `quantity * unit_price - discount_amount`, and payments reconcile
  against order totals.

## Note on Liquibase checksums
Both changesets are edited in place, so existing dev databases will fail the checksum check.
Recreate the local DB (or run `liquibase clearCheckSums`). Testcontainers starts clean, so
integration tests are unaffected.
