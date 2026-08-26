-- Basic schema (used by Testcontainers), modify as needed for your application

CREATE TABLE public.customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customer can have many jobsites -> job sites can have many quotes
CREATE TABLE public.jobsites (
    id BIGSERIAL PRIMARY KEY,
    customerId BIGSERIAL NOT NULL,
    address VARCHAR(255) NOT NULL,
    zipcode VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    expected_completion TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Many quotes AND many orders
CREATE TABLE public.quotes (
    id BIGSERIAL PRIMARY KEY,
    jobsite_id BIGSERIAL NOT NULL,
    customerId BIGSERIAL NOT NULL,
    email VARCHAR(255) UNIQUE,
    estimate BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders need site and customer, don't need a quote for quick moving
CREATE TABLE public.orders (
    id BIGSERIAL PRIMARY KEY,
    jobsite_id BIGSERIAL NOT NULL,
    customerId BIGSERIAL NOT NULL,
    quote_id  BIGSERIAL,
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);