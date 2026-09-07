--liquibase formatted sql

--changeset robertsima:seed-starter-data-001
--comment: Seed one full walk through the domain - businesses, users, jobsites, providers with
--comment: inventory and volume pricing, then a quote converted to an order and paid.
--comment: Ids are explicit so cross-table references stay readable; every insert is
--comment: ON CONFLICT DO NOTHING, and the BIGSERIAL sequences are resynced at the bottom.

-- ---------------------------------------------------------------------------
-- Businesses
-- ---------------------------------------------------------------------------
INSERT INTO public.businesses (id, name, business_type, tax_id, email, phone, address, city, state, zipcode, country)
VALUES
    (1, 'Acme Construction', 'CONTRACTOR', '81-2345678', 'contact@acmeconstruction.test', '212-555-0110',
     '400 Industrial Way', 'New York', 'NY', '10001', 'USA'),
    (2, 'Northwind Builders', 'CONTRACTOR', '82-3456789', 'hello@northwindbuilders.test', '215-555-0142',
     '18 Foundry Street', 'Philadelphia', 'PA', '19103', 'USA')
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Users - user_type is what the RBAC layer reads
-- ---------------------------------------------------------------------------
INSERT INTO public.users (id, business_id, first_name, last_name, email, phone, user_type)
VALUES
    (1, 1, 'Dana',   'Reyes',  'dana.reyes@acmeconstruction.test',      '212-555-0111', 'ACCOUNT_ADMIN'),
    (2, 1, 'Marcus', 'Hale',   'marcus.hale@acmeconstruction.test',     '212-555-0112', 'JOBSITE_ADMIN'),
    (3, 1, 'Priya',  'Raman',  'priya.raman@acmeconstruction.test',     '212-555-0113', 'STAFF_USER'),
    (4, 2, 'Owen',   'Voss',   'owen.voss@northwindbuilders.test',      '215-555-0143', 'ACCOUNT_ADMIN'),
    (5, 2, 'Lena',   'Ortiz',  'lena.ortiz@northwindbuilders.test',     '215-555-0144', 'STAFF_USER')
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Jobsites
-- ---------------------------------------------------------------------------
INSERT INTO public.jobsites (id, business_id, user_id, name, jobsite_type, address, city, state, zipcode, country,
                             start_date, expected_completion)
VALUES
    (1, 1, 2, 'Main Street Mixed Use', 'COMMERCIAL',
     '100 Main Street', 'New York', 'NY', '10001', 'USA',
     TIMESTAMPTZ '2026-03-02 08:00:00+00', TIMESTAMPTZ '2026-10-15 17:00:00+00'),
    (2, 1, 3, 'Riverside Townhomes', 'RESIDENTIAL',
     '77 Riverside Drive', 'Jersey City', 'NJ', '07302', 'USA',
     TIMESTAMPTZ '2026-05-11 08:00:00+00', TIMESTAMPTZ '2027-01-29 17:00:00+00'),
    (3, 2, 5, 'Market Avenue Warehouse', 'INDUSTRIAL',
     '250 Market Avenue', 'Philadelphia', 'PA', '19103', 'USA',
     TIMESTAMPTZ '2026-06-01 08:00:00+00', TIMESTAMPTZ '2026-11-01 17:00:00+00')
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Providers and the businesses integrated with them
-- ---------------------------------------------------------------------------
INSERT INTO public.providers (id, name, code, business_type, contact_email, phone, website, api_base_url)
VALUES
    (1, 'Home Depot Pro', 'HOME_DEPOT', 'RETAIL_MERCHANT',
     'pro@homedepot.test', '800-555-0100', 'https://pro.homedepot.test', 'https://api.homedepot.test/v1'),
    (2, 'Ferguson Supply', 'FERGUSON', 'WHOLESALE_SUPPLIER',
     'orders@ferguson.test', '800-555-0200', 'https://ferguson.test', 'https://api.ferguson.test/v2'),
    (3, '84 Lumber', 'EIGHTY_FOUR_LUMBER', 'WHOLESALE_SUPPLIER',
     'sales@84lumber.test', '800-555-0300', 'https://84lumber.test', 'https://api.84lumber.test/v1')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.business_providers (business_id, provider_id)
VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 1), (2, 2)
ON CONFLICT (business_id, provider_id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Inventories - one per provider
-- ---------------------------------------------------------------------------
INSERT INTO public.inventories (id, provider_id, name, last_restocked_at)
VALUES
    (1, 1, 'Home Depot Pro - Northeast Catalog', TIMESTAMPTZ '2026-09-01 06:00:00+00'),
    (2, 2, 'Ferguson - Plumbing Catalog',        TIMESTAMPTZ '2026-09-03 06:00:00+00'),
    (3, 3, '84 Lumber - Framing Catalog',        TIMESTAMPTZ '2026-08-28 06:00:00+00')
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Items
-- ---------------------------------------------------------------------------
INSERT INTO public.items (id, inventory_id, sku, name, description, category, brand, unit_of_measure,
                          price, color, material, dimensions, weight, stock_quantity)
VALUES
    (1, 1, 'HD-2X4-8',      '2x4x8 SPF Stud',
     'Kiln dried spruce-pine-fir framing stud.', 'LUMBER', 'ProBuild', 'EACH',
     4.2800, 'Natural', 'Softwood', '1.5in x 3.5in x 8ft', '9.2 lb', 8400),

    (2, 1, 'HD-PLY-12',     '1/2in Plywood Sheathing 4x8',
     'CDX rated sheathing panel.', 'SHEET_GOODS', 'ProBuild', 'EACH',
     32.7500, 'Natural', 'Plywood', '48in x 96in x 0.5in', '40.6 lb', 1250),

    (3, 1, 'HD-SCR-3IN',    '3in Construction Screws, 5 lb Box',
     'Star drive exterior coated screws.', 'FASTENERS', 'GripFast', 'BOX',
     28.9700, 'Bronze', 'Coated Steel', '3in', '5 lb', 640),

    (4, 1, 'HD-CONC-60',    '60 lb Concrete Mix',
     'General purpose 4000 psi concrete mix.', 'CONCRETE', 'SetRite', 'EACH',
     6.4800, 'Gray', 'Portland Cement', '60 lb bag', '60 lb', 3100),

    (5, 2, 'FG-PVC-4IN',    '4in PVC DWV Pipe',
     'Schedule 40 drain waste vent pipe.', 'PLUMBING', 'Charlotte', 'LINEAR_FOOT',
     18.4000, 'White', 'PVC', '4in diameter', '2.1 lb/ft', 2600),

    (6, 2, 'FG-CU-075',     '3/4in Type L Copper Tube',
     'Hard drawn copper water tube.', 'PLUMBING', 'Mueller', 'LINEAR_FOOT',
     6.9500, 'Copper', 'Copper', '0.75in diameter', '0.42 lb/ft', 4800),

    (7, 2, 'FG-BALL-1IN',   '1in Brass Ball Valve',
     'Full port lead free brass ball valve.', 'PLUMBING', 'Apollo', 'EACH',
     21.3000, 'Brass', 'Brass', '1in NPT', '1.1 lb', 720),

    (8, 3, 'EL-STUD-92',    '2x4x92-5/8 Precut Stud',
     'Precut wall stud for 8ft plate height.', 'LUMBER', 'MillDirect', 'EACH',
     3.9400, 'Natural', 'Softwood', '1.5in x 3.5in x 92.625in', '8.8 lb', 12000),

    (9, 3, 'EL-OSB-716',    '7/16in OSB Sheathing 4x8',
     'Structural 1 rated oriented strand board.', 'SHEET_GOODS', 'MillDirect', 'EACH',
     21.8500, 'Natural', 'OSB', '48in x 96in x 0.4375in', '46.0 lb', 2200),

    (10, 3, 'EL-JOIST-2X10', '2x10x16 #2 SYP Joist',
     'Southern yellow pine dimensional joist.', 'LUMBER', 'MillDirect', 'EACH',
     27.6000, 'Natural', 'Softwood', '1.5in x 9.25in x 16ft', '38.4 lb', 960)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Volume discount hierarchies - exactly one per provider - and their tiers
-- ---------------------------------------------------------------------------
INSERT INTO public.volume_discount_hierarchies (id, provider_id, name, description)
VALUES
    (1, 1, 'Home Depot Pro Volume Pricing', 'Three tier contractor pricing on catalog quantities.'),
    (2, 2, 'Ferguson Wholesale Tiers',      'Wholesale breaks for plumbing quantities.'),
    (3, 3, '84 Lumber Framing Tiers',       'Framing package breaks for bulk lumber pulls.')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.volume_discount_tiers (id, hierarchy_id, level, min_quantity, discount_percentage, label)
VALUES
    (1, 1, 1,  100,  5.00, 'Pro'),
    (2, 1, 2,  250,  7.50, 'Pro Plus'),
    (3, 1, 3,  500, 10.00, 'Pro Elite'),

    (4, 2, 1,  100,  4.00, 'Trade'),
    (5, 2, 2,  250,  8.00, 'Trade Plus'),
    (6, 2, 3,  500, 12.00, 'Trade Elite'),

    (7, 3, 1,  150,  3.00, 'Framer'),
    (8, 3, 2,  400,  6.00, 'Framer Plus'),
    (9, 3, 3, 1000,  9.00, 'Framer Elite')
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Discounts - the price decorators, scoped to a provider or to a single item
-- ---------------------------------------------------------------------------
INSERT INTO public.discounts (id, provider_id, item_id, code, name, description, discount_type, value,
                              minimum_order_total, starts_at, ends_at)
VALUES
    (1, 1, NULL, 'HD-SPRING-2026', 'Spring Pro Event',
     'Percent off any Home Depot Pro order over the threshold.', 'PERCENTAGE', 7.5000,
     5000.0000, TIMESTAMPTZ '2026-03-01 00:00:00+00', TIMESTAMPTZ '2026-12-31 23:59:59+00'),

    (2, 1, 4, 'HD-CONC-BULK', 'Concrete Mix Bulk Break',
     'Item scoped percent off 60 lb concrete mix.', 'PERCENTAGE', 5.0000,
     NULL, TIMESTAMPTZ '2026-01-01 00:00:00+00', NULL),

    (3, 2, NULL, 'FG-NET-FREIGHT', 'Freight Credit',
     'Flat freight credit on qualifying wholesale orders.', 'FIXED_AMOUNT', 125.0000,
     2500.0000, TIMESTAMPTZ '2026-01-01 00:00:00+00', NULL),

    (4, 3, NULL, 'EL-CONTRACTOR', 'Contractor Card',
     'Standing percent off for carded contractors.', 'PERCENTAGE', 4.0000,
     NULL, TIMESTAMPTZ '2026-01-01 00:00:00+00', NULL)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Quotes - one per provider relationship being exercised
-- quote 1 has been converted to an order, quote 2 is still being built, quote 3 is a draft
-- ---------------------------------------------------------------------------
INSERT INTO public.quotes (id, quote_number, business_id, jobsite_id, provider_id, user_id, status,
                           subtotal, discount_total, tax_total, total, notes, valid_until)
VALUES
    (1, 'Q-2026-0001', 1, 1, 1, 2, 'ORDER',
     7366.0000, 475.3000, 482.3500, 7373.0500,
     'Framing and slab package for the Main Street podium level.',
     TIMESTAMPTZ '2026-10-01 23:59:59+00'),

    (2, 'Q-2026-0002', 1, 2, 2, 3, 'IN_PROGRESS',
     5969.7000, 218.3400, 402.6000, 6153.9600,
     'Rough plumbing takeoff for townhome buildings A and B.',
     TIMESTAMPTZ '2026-11-15 23:59:59+00'),

    (3, 'Q-2026-0003', 2, 3, 1, 5, 'DRAFT',
     690.0400, 0.0000, 48.3000, 738.3400,
     'Fastener and stud top-up for the warehouse mezzanine.',
     TIMESTAMPTZ '2026-10-20 23:59:59+00')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.quote_line_items (id, quote_id, item_id, sku, description, unit_of_measure,
                                     quantity, unit_price, discount_amount, line_total)
VALUES
    -- Q-2026-0001: 500 studs hit the Pro Elite tier (10%), the rest land on Pro (5%)
    (1, 1, 1, 'HD-2X4-8',   '2x4x8 SPF Stud',                'EACH', 500,  4.2800, 214.0000, 1926.0000),
    (2, 1, 2, 'HD-PLY-12',  '1/2in Plywood Sheathing 4x8',   'EACH', 120, 32.7500, 196.5000, 3733.5000),
    (3, 1, 4, 'HD-CONC-60', '60 lb Concrete Mix',            'EACH', 200,  6.4800,  64.8000, 1231.2000),

    -- Q-2026-0002: Trade tier (4%) on the two bulk plumbing lines
    (4, 2, 5, 'FG-PVC-4IN',  '4in PVC DWV Pipe',             'LINEAR_FOOT', 240, 18.4000, 176.6400, 4239.3600),
    (5, 2, 6, 'FG-CU-075',   '3/4in Type L Copper Tube',     'LINEAR_FOOT', 150,  6.9500,  41.7000, 1000.8000),
    (6, 2, 7, 'FG-BALL-1IN', '1in Brass Ball Valve',         'EACH',         24, 21.3000,   0.0000,  511.2000),

    -- Q-2026-0003: below every tier minimum, so nothing is discounted
    (7, 3, 3, 'HD-SCR-3IN', '3in Construction Screws, 5 lb Box', 'BOX',  12, 28.9700, 0.0000, 347.6400),
    (8, 3, 1, 'HD-2X4-8',   '2x4x8 SPF Stud',                    'EACH', 80,  4.2800, 0.0000, 342.4000)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Orders - order 1 came from quote 1, order 2 was placed directly
-- ---------------------------------------------------------------------------
INSERT INTO public.orders (id, order_number, business_id, jobsite_id, provider_id, user_id, quote_id, status,
                           subtotal, discount_total, tax_total, shipping_total, total,
                           delivery_address, notes, submitted_at, expected_delivery, delivered_at)
VALUES
    (1, 'O-2026-0001', 1, 1, 1, 2, 1, 'SHIPPING',
     7366.0000, 475.3000, 482.3500, 250.0000, 7623.0500,
     '100 Main Street, New York, NY 10001, USA',
     'Converted from Q-2026-0001. Deliver to the north gate before 07:00.',
     TIMESTAMPTZ '2026-09-02 14:20:00+00', TIMESTAMPTZ '2026-09-12 12:00:00+00', NULL),

    (2, 'O-2026-0002', 2, 3, 2, 4, NULL, 'PAYMENT_RECEIVED',
     2937.0000, 166.8000, 193.9100, 125.0000, 3089.1100,
     '250 Market Avenue, Philadelphia, PA 19103, USA',
     'Direct order, no quote. Copper for the mezzanine wet wall.',
     TIMESTAMPTZ '2026-09-04 09:05:00+00', TIMESTAMPTZ '2026-09-16 12:00:00+00', NULL)
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.order_line_items (id, order_id, item_id, sku, description, unit_of_measure,
                                     quantity, quantity_fulfilled, unit_price, discount_amount, line_total)
VALUES
    -- O-2026-0001 mirrors the converted quote; studs and plywood already shipped
    (1, 1, 1, 'HD-2X4-8',   '2x4x8 SPF Stud',              'EACH', 500, 500,  4.2800, 214.0000, 1926.0000),
    (2, 1, 2, 'HD-PLY-12',  '1/2in Plywood Sheathing 4x8', 'EACH', 120, 120, 32.7500, 196.5000, 3733.5000),
    (3, 1, 4, 'HD-CONC-60', '60 lb Concrete Mix',          'EACH', 200,   0,  6.4800,  64.8000, 1231.2000),

    -- O-2026-0002: 300 ft of copper reaches the Trade Plus tier (8%)
    (4, 2, 6, 'FG-CU-075',   '3/4in Type L Copper Tube', 'LINEAR_FOOT', 300, 300, 6.9500, 166.8000, 1918.2000),
    (5, 2, 7, 'FG-BALL-1IN', '1in Brass Ball Valve',      'EACH',        40,  40, 21.3000,  0.0000,  852.0000)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Payments - transaction_reference is the idempotency key
-- Order 1 is half captured with the balance still pending; order 2 is paid in full.
-- ---------------------------------------------------------------------------
INSERT INTO public.payments (id, order_id, transaction_reference, payment_amount, payment_type, payment_status,
                             payment_date, processed_at, failure_reason)
VALUES
    (1, 1, 'TXN-ACME-0001', 3811.5300, 'PC',  'SUCCESSFUL',
     TIMESTAMPTZ '2026-09-02 14:21:00+00', TIMESTAMPTZ '2026-09-02 14:21:06+00', NULL),

    (2, 1, 'TXN-ACME-0002', 3811.5200, 'ACH', 'PENDING',
     TIMESTAMPTZ '2026-09-06 10:00:00+00', NULL, NULL),

    (3, 2, 'TXN-NWND-0001', 3089.1100, 'ACH', 'SUCCESSFUL',
     TIMESTAMPTZ '2026-09-04 09:06:00+00', TIMESTAMPTZ '2026-09-05 02:14:00+00', NULL)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Resync the BIGSERIAL sequences past the explicit ids seeded above
-- ---------------------------------------------------------------------------
SELECT setval(pg_get_serial_sequence('public.businesses', 'id'),                 COALESCE((SELECT MAX(id) FROM public.businesses), 1));
SELECT setval(pg_get_serial_sequence('public.users', 'id'),                      COALESCE((SELECT MAX(id) FROM public.users), 1));
SELECT setval(pg_get_serial_sequence('public.jobsites', 'id'),                   COALESCE((SELECT MAX(id) FROM public.jobsites), 1));
SELECT setval(pg_get_serial_sequence('public.providers', 'id'),                  COALESCE((SELECT MAX(id) FROM public.providers), 1));
SELECT setval(pg_get_serial_sequence('public.inventories', 'id'),                COALESCE((SELECT MAX(id) FROM public.inventories), 1));
SELECT setval(pg_get_serial_sequence('public.items', 'id'),                      COALESCE((SELECT MAX(id) FROM public.items), 1));
SELECT setval(pg_get_serial_sequence('public.volume_discount_hierarchies', 'id'), COALESCE((SELECT MAX(id) FROM public.volume_discount_hierarchies), 1));
SELECT setval(pg_get_serial_sequence('public.volume_discount_tiers', 'id'),      COALESCE((SELECT MAX(id) FROM public.volume_discount_tiers), 1));
SELECT setval(pg_get_serial_sequence('public.discounts', 'id'),                  COALESCE((SELECT MAX(id) FROM public.discounts), 1));
SELECT setval(pg_get_serial_sequence('public.quotes', 'id'),                     COALESCE((SELECT MAX(id) FROM public.quotes), 1));
SELECT setval(pg_get_serial_sequence('public.quote_line_items', 'id'),           COALESCE((SELECT MAX(id) FROM public.quote_line_items), 1));
SELECT setval(pg_get_serial_sequence('public.orders', 'id'),                     COALESCE((SELECT MAX(id) FROM public.orders), 1));
SELECT setval(pg_get_serial_sequence('public.order_line_items', 'id'),           COALESCE((SELECT MAX(id) FROM public.order_line_items), 1));
SELECT setval(pg_get_serial_sequence('public.payments', 'id'),                   COALESCE((SELECT MAX(id) FROM public.payments), 1));
