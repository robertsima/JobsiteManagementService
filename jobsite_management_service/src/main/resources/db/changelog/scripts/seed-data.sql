--liquibase formatted sql

--changeset robertsima:seed-starter-data-001
--comment: Seed starter customers, jobsites, quotes, and orders with proper foreign-key references.

WITH seeded_customers AS (
INSERT INTO public.customers (name, email)
VALUES
    ('Acme Construction', 'contact@acmeconstruction.test'),
    ('Northwind Builders', 'hello@northwindbuilders.test'),
    ('Blue Ridge Renovations', 'info@blueridgereno.test')
ON CONFLICT (email) DO UPDATE
                           SET name = EXCLUDED.name
                           RETURNING id, email
                           ),

                           all_customers AS (
                       SELECT id, email
                       FROM seeded_customers

                       UNION

                       SELECT id, email
                       FROM public.customers
                       WHERE email IN (
                           'contact@acmeconstruction.test',
                           'hello@northwindbuilders.test',
                           'info@blueridgereno.test'
                           )
                           ),

                           seeded_jobsites AS (
                       INSERT INTO public.jobsites (
    customer_id,
    address,
    zipcode,
    city,
    state,
    country,
    expected_completion
)
                       SELECT
                           c.id,
                           v.address,
                           v.zipcode,
                           v.city,
                           v.state,
                           v.country,
                           v.expected_completion
                       FROM (
                           VALUES
                           (
                           'contact@acmeconstruction.test',
                           '100 Main Street',
                           '10001',
                           'New York',
                           'NY',
                           'USA',
                           TIMESTAMP '2026-10-15 17:00:00'
                           ),
                           (
                           'hello@northwindbuilders.test',
                           '250 Market Avenue',
                           '19103',
                           'Philadelphia',
                           'PA',
                           'USA',
                           TIMESTAMP '2026-11-01 17:00:00'
                           ),
                           (
                           'info@blueridgereno.test',
                           '88 Ridge Road',
                           '28801',
                           'Asheville',
                           'NC',
                           'USA',
                           TIMESTAMP '2026-12-05 17:00:00'
                           )
                           ) AS v (
                           customer_email,
                           address,
                           zipcode,
                           city,
                           state,
                           country,
                           expected_completion
                           )
                           JOIN all_customers c
                       ON c.email = v.customer_email
                       WHERE NOT EXISTS (
                           SELECT 1
                           FROM public.jobsites j
                           WHERE j.customer_id = c.id
                         AND j.address = v.address
                         AND j.zipcode = v.zipcode
                           )
                           RETURNING id, customer_id, address, zipcode
                           ),

                           all_jobsites AS (
                       SELECT id, customer_id, address, zipcode
                       FROM seeded_jobsites

                       UNION

                       SELECT j.id, j.customer_id, j.address, j.zipcode
                       FROM public.jobsites j
                           JOIN all_customers c
                       ON c.id = j.customer_id
                       WHERE j.address IN (
                           '100 Main Street',
                           '250 Market Avenue',
                           '88 Ridge Road'
                           )
                           ),

                           seeded_quotes AS (
                       INSERT INTO public.quotes (
    jobsite_id,
    customer_id,
    email,
    estimate
)
                       SELECT
                           j.id,
                           j.customer_id,
                           v.quote_email,
                           v.estimate
                       FROM (
                           VALUES
                           ('100 Main Street', 'quote-acme-001@test.local', 125000),
                           ('250 Market Avenue', 'quote-northwind-001@test.local', 87500),
                           ('88 Ridge Road', 'quote-blueridge-001@test.local', 43000)
                           ) AS v (
                           jobsite_address,
                           quote_email,
                           estimate
                           )
                           JOIN all_jobsites j
                       ON j.address = v.jobsite_address
                       WHERE NOT EXISTS (
                           SELECT 1
                           FROM public.quotes q
                           WHERE q.email = v.quote_email
                           )
                           RETURNING id, jobsite_id, customer_id, email
                           ),

                           all_quotes AS (
                       SELECT id, jobsite_id, customer_id, email
                       FROM seeded_quotes

                       UNION

                       SELECT id, jobsite_id, customer_id, email
                       FROM public.quotes
                       WHERE email IN (
                           'quote-acme-001@test.local',
                           'quote-northwind-001@test.local',
                           'quote-blueridge-001@test.local'
                           )
                           )

                       INSERT INTO public.orders (
    jobsite_id,
    customer_id,
    quote_id,
    email
)
SELECT
    q.jobsite_id,
    q.customer_id,
    q.id,
    v.order_email
FROM (
         VALUES
             ('quote-acme-001@test.local', 'order-acme-001@test.local'),
             ('quote-northwind-001@test.local', 'order-northwind-001@test.local'),
             ('quote-blueridge-001@test.local', 'order-blueridge-001@test.local')
     ) AS v (
             quote_email,
             order_email
    )
         JOIN all_quotes q
              ON q.email = v.quote_email
WHERE NOT EXISTS (
    SELECT 1
    FROM public.orders o
    WHERE o.email = v.order_email
);