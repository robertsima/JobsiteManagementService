-- Jobsite Management Service schema.
-- Mirrors com.jobsite_management_service.model.entity.*; tables are created in FK-safe order.
-- NOTE: this changeset is edited in place, so an existing dev database will fail Liquibase
-- checksum validation. Recreate the database or run `liquibase clearCheckSums`.
-- Testcontainers always starts from an empty database, so integration tests are unaffected.

-- ---------------------------------------------------------------------------
-- Businesses: the composite root. A business owns its users and jobsites.
-- ---------------------------------------------------------------------------
CREATE TABLE public.businesses (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    business_type VARCHAR(64)  NOT NULL,
    tax_id        VARCHAR(64)  UNIQUE,
    email         VARCHAR(255),
    phone         VARCHAR(64),
    address       VARCHAR(255),
    city          VARCHAR(255),
    state         VARCHAR(255),
    zipcode       VARCHAR(32),
    country       VARCHAR(255),
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------------
-- Users: belong to one business; user_type drives role based access control.
-- ---------------------------------------------------------------------------
CREATE TABLE public.users (
    id          BIGSERIAL PRIMARY KEY,
    business_id BIGINT,
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    phone       VARCHAR(64),
    user_type   VARCHAR(64)  NOT NULL,
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_business
        FOREIGN KEY (business_id) REFERENCES public.businesses(id) ON DELETE CASCADE
);

CREATE INDEX idx_users_business ON public.users(business_id);

-- ---------------------------------------------------------------------------
-- Jobsites: a business can have many; each names the user who requested it.
-- ---------------------------------------------------------------------------
CREATE TABLE public.jobsites (
    id                  BIGSERIAL PRIMARY KEY,
    business_id         BIGINT,
    user_id             BIGINT,
    name                VARCHAR(255) NOT NULL,
    jobsite_type        VARCHAR(64)  NOT NULL,
    address             VARCHAR(255) NOT NULL,
    address_line_2      VARCHAR(255),
    city                VARCHAR(255) NOT NULL,
    state               VARCHAR(255) NOT NULL,
    zipcode             VARCHAR(32)  NOT NULL,
    country             VARCHAR(255) NOT NULL,
    start_date          TIMESTAMPTZ,
    expected_completion TIMESTAMPTZ,
    active              BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_jobsites_business
        FOREIGN KEY (business_id) REFERENCES public.businesses(id) ON DELETE CASCADE,

    CONSTRAINT fk_jobsites_user
        FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE SET NULL
);

CREATE INDEX idx_jobsites_business ON public.jobsites(business_id);
CREATE INDEX idx_jobsites_user ON public.jobsites(user_id);

-- ---------------------------------------------------------------------------
-- Providers: suppliers such as Home Depot. api_base_url is the integration hook.
-- ---------------------------------------------------------------------------
CREATE TABLE public.providers (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    code          VARCHAR(64)  NOT NULL UNIQUE,
    business_type VARCHAR(64)  NOT NULL,
    contact_email VARCHAR(255),
    phone         VARCHAR(64),
    website       VARCHAR(255),
    api_base_url  VARCHAR(255),
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP
);

-- Many-to-many: which providers a business has been granted access to.
CREATE TABLE public.business_providers (
    business_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,

    CONSTRAINT pk_business_providers PRIMARY KEY (business_id, provider_id),

    CONSTRAINT fk_business_providers_business
        FOREIGN KEY (business_id) REFERENCES public.businesses(id) ON DELETE CASCADE,

    CONSTRAINT fk_business_providers_provider
        FOREIGN KEY (provider_id) REFERENCES public.providers(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------------
-- Inventory: exactly one per provider.
-- ---------------------------------------------------------------------------
CREATE TABLE public.inventories (
    id                BIGSERIAL PRIMARY KEY,
    provider_id       BIGINT       NOT NULL UNIQUE,
    name              VARCHAR(255) NOT NULL,
    last_restocked_at TIMESTAMPTZ,
    created_at        TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_inventories_provider
        FOREIGN KEY (provider_id) REFERENCES public.providers(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------------
-- Items: one SKU inside one inventory. Facets here back the search/filter feature.
-- ---------------------------------------------------------------------------
CREATE TABLE public.items (
    id              BIGSERIAL PRIMARY KEY,
    inventory_id    BIGINT        NOT NULL,
    sku             VARCHAR(64)   NOT NULL,
    name            VARCHAR(255)  NOT NULL,
    description     VARCHAR(1024),
    category        VARCHAR(128),
    brand           VARCHAR(128),
    unit_of_measure VARCHAR(32)   NOT NULL DEFAULT 'EACH',
    price           NUMERIC(19,4) NOT NULL DEFAULT 0,
    currency        VARCHAR(3)    NOT NULL DEFAULT 'USD',
    color           VARCHAR(64),
    material        VARCHAR(128),
    dimensions      VARCHAR(128),
    weight          VARCHAR(64),
    stock_quantity  INTEGER       NOT NULL DEFAULT 0,
    active          BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_items_inventory_sku UNIQUE (inventory_id, sku),

    CONSTRAINT fk_items_inventory
        FOREIGN KEY (inventory_id) REFERENCES public.inventories(id) ON DELETE CASCADE
);

CREATE INDEX idx_items_inventory ON public.items(inventory_id);
CREATE INDEX idx_items_category ON public.items(category);

-- ---------------------------------------------------------------------------
-- Volume discount hierarchy: singleton per provider, made of ordered tiers.
-- ---------------------------------------------------------------------------
CREATE TABLE public.volume_discount_hierarchies (
    id          BIGSERIAL PRIMARY KEY,
    provider_id BIGINT       NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(1024),
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_vdh_provider
        FOREIGN KEY (provider_id) REFERENCES public.providers(id) ON DELETE CASCADE
);

CREATE TABLE public.volume_discount_tiers (
    id                  BIGSERIAL PRIMARY KEY,
    hierarchy_id        BIGINT       NOT NULL,
    level               INTEGER      NOT NULL,
    min_quantity        INTEGER      NOT NULL,
    discount_percentage NUMERIC(5,2) NOT NULL DEFAULT 0,
    label               VARCHAR(128),

    CONSTRAINT uq_volume_tier_level UNIQUE (hierarchy_id, level),

    CONSTRAINT fk_vdt_hierarchy
        FOREIGN KEY (hierarchy_id) REFERENCES public.volume_discount_hierarchies(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------------
-- Discounts: decorator applied to a price. Scoped to a provider, an item, or global.
-- ---------------------------------------------------------------------------
CREATE TABLE public.discounts (
    id                  BIGSERIAL PRIMARY KEY,
    provider_id         BIGINT,
    item_id             BIGINT,
    code                VARCHAR(64)   NOT NULL UNIQUE,
    name                VARCHAR(255)  NOT NULL,
    description         VARCHAR(1024),
    discount_type       VARCHAR(32)   NOT NULL DEFAULT 'PERCENTAGE',
    value               NUMERIC(19,4) NOT NULL DEFAULT 0,
    minimum_order_total NUMERIC(19,4),
    active              BOOLEAN       NOT NULL DEFAULT TRUE,
    starts_at           TIMESTAMPTZ,
    ends_at             TIMESTAMPTZ,
    created_at          TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_discounts_provider
        FOREIGN KEY (provider_id) REFERENCES public.providers(id) ON DELETE CASCADE,

    CONSTRAINT fk_discounts_item
        FOREIGN KEY (item_id) REFERENCES public.items(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------------
-- Quotes: one request against exactly one provider.
-- ---------------------------------------------------------------------------
CREATE TABLE public.quotes (
    id             BIGSERIAL PRIMARY KEY,
    quote_number   VARCHAR(64)   NOT NULL UNIQUE,
    business_id    BIGINT        NOT NULL,
    jobsite_id     BIGINT        NOT NULL,
    provider_id    BIGINT        NOT NULL,
    user_id        BIGINT        NOT NULL,
    status         VARCHAR(32)   NOT NULL DEFAULT 'DRAFT',
    currency       VARCHAR(3)    NOT NULL DEFAULT 'USD',
    subtotal       NUMERIC(19,4) NOT NULL DEFAULT 0,
    discount_total NUMERIC(19,4) NOT NULL DEFAULT 0,
    tax_total      NUMERIC(19,4) NOT NULL DEFAULT 0,
    total          NUMERIC(19,4) NOT NULL DEFAULT 0,
    notes          VARCHAR(2048),
    valid_until    TIMESTAMPTZ,
    created_at     TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_quotes_business
        FOREIGN KEY (business_id) REFERENCES public.businesses(id) ON DELETE CASCADE,

    CONSTRAINT fk_quotes_jobsite
        FOREIGN KEY (jobsite_id) REFERENCES public.jobsites(id) ON DELETE CASCADE,

    CONSTRAINT fk_quotes_provider
        FOREIGN KEY (provider_id) REFERENCES public.providers(id) ON DELETE RESTRICT,

    CONSTRAINT fk_quotes_user
        FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE INDEX idx_quotes_business ON public.quotes(business_id);
CREATE INDEX idx_quotes_jobsite ON public.quotes(jobsite_id);
CREATE INDEX idx_quotes_status ON public.quotes(status);

CREATE TABLE public.quote_line_items (
    id              BIGSERIAL PRIMARY KEY,
    quote_id        BIGINT        NOT NULL,
    item_id         BIGINT,
    sku             VARCHAR(64)   NOT NULL,
    description     VARCHAR(255)  NOT NULL,
    unit_of_measure VARCHAR(32)   NOT NULL DEFAULT 'EACH',
    quantity        INTEGER       NOT NULL DEFAULT 0,
    unit_price      NUMERIC(19,4) NOT NULL DEFAULT 0,
    discount_amount NUMERIC(19,4) NOT NULL DEFAULT 0,
    line_total      NUMERIC(19,4) NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_quote_line_items_quote
        FOREIGN KEY (quote_id) REFERENCES public.quotes(id) ON DELETE CASCADE,

    CONSTRAINT fk_quote_line_items_item
        FOREIGN KEY (item_id) REFERENCES public.items(id) ON DELETE SET NULL
);

CREATE INDEX idx_quote_line_items_quote ON public.quote_line_items(quote_id);

-- ---------------------------------------------------------------------------
-- Orders: need a site and a user, but a quote is optional.
-- ---------------------------------------------------------------------------
CREATE TABLE public.orders (
    id                BIGSERIAL PRIMARY KEY,
    order_number      VARCHAR(64)   NOT NULL UNIQUE,
    business_id       BIGINT        NOT NULL,
    jobsite_id        BIGINT        NOT NULL,
    provider_id       BIGINT        NOT NULL,
    user_id           BIGINT        NOT NULL,
    quote_id          BIGINT,
    status            VARCHAR(32)   NOT NULL DEFAULT 'DRAFT',
    currency          VARCHAR(3)    NOT NULL DEFAULT 'USD',
    subtotal          NUMERIC(19,4) NOT NULL DEFAULT 0,
    discount_total    NUMERIC(19,4) NOT NULL DEFAULT 0,
    tax_total         NUMERIC(19,4) NOT NULL DEFAULT 0,
    shipping_total    NUMERIC(19,4) NOT NULL DEFAULT 0,
    total             NUMERIC(19,4) NOT NULL DEFAULT 0,
    delivery_address  VARCHAR(255),
    notes             VARCHAR(2048),
    submitted_at      TIMESTAMPTZ,
    expected_delivery TIMESTAMPTZ,
    delivered_at      TIMESTAMPTZ,
    created_at        TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_orders_business
        FOREIGN KEY (business_id) REFERENCES public.businesses(id) ON DELETE CASCADE,

    CONSTRAINT fk_orders_jobsite
        FOREIGN KEY (jobsite_id) REFERENCES public.jobsites(id) ON DELETE CASCADE,

    CONSTRAINT fk_orders_provider
        FOREIGN KEY (provider_id) REFERENCES public.providers(id) ON DELETE RESTRICT,

    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE,

    CONSTRAINT fk_orders_quote
        FOREIGN KEY (quote_id) REFERENCES public.quotes(id) ON DELETE SET NULL
);

CREATE INDEX idx_orders_business ON public.orders(business_id);
CREATE INDEX idx_orders_jobsite ON public.orders(jobsite_id);
CREATE INDEX idx_orders_status ON public.orders(status);

CREATE TABLE public.order_line_items (
    id                 BIGSERIAL PRIMARY KEY,
    order_id           BIGINT        NOT NULL,
    item_id            BIGINT,
    sku                VARCHAR(64)   NOT NULL,
    description        VARCHAR(255)  NOT NULL,
    unit_of_measure    VARCHAR(32)   NOT NULL DEFAULT 'EACH',
    quantity           INTEGER       NOT NULL DEFAULT 0,
    quantity_fulfilled INTEGER       NOT NULL DEFAULT 0,
    unit_price         NUMERIC(19,4) NOT NULL DEFAULT 0,
    discount_amount    NUMERIC(19,4) NOT NULL DEFAULT 0,
    line_total         NUMERIC(19,4) NOT NULL DEFAULT 0,
    created_at         TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_line_items_order
        FOREIGN KEY (order_id) REFERENCES public.orders(id) ON DELETE CASCADE,

    CONSTRAINT fk_order_line_items_item
        FOREIGN KEY (item_id) REFERENCES public.items(id) ON DELETE SET NULL
);

CREATE INDEX idx_order_line_items_order ON public.order_line_items(order_id);

-- ---------------------------------------------------------------------------
-- Payments: transaction_reference is the idempotency key for a retried capture.
-- ---------------------------------------------------------------------------
CREATE TABLE public.payments (
    id                    BIGSERIAL PRIMARY KEY,
    order_id              BIGINT        NOT NULL,
    transaction_reference VARCHAR(128)  NOT NULL UNIQUE,
    payment_amount        NUMERIC(19,4) NOT NULL DEFAULT 0,
    currency              VARCHAR(3)    NOT NULL DEFAULT 'USD',
    payment_type          VARCHAR(32)   NOT NULL,
    payment_status        VARCHAR(32)   NOT NULL DEFAULT 'PENDING',
    payment_date          TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at          TIMESTAMPTZ,
    failure_reason        VARCHAR(255),
    created_at            TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id) REFERENCES public.orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_payments_order ON public.payments(order_id);
CREATE INDEX idx_payments_status ON public.payments(payment_status);
