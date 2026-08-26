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
                                 customer_id BIGINT NOT NULL,
                                 address VARCHAR(255) NOT NULL,
                                 zipcode VARCHAR(255) NOT NULL,
                                 city VARCHAR(255) NOT NULL,
                                 state VARCHAR(255) NOT NULL,
                                 country VARCHAR(255) NOT NULL,
                                 expected_completion TIMESTAMP,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT fk_jobsites_customer
                                     FOREIGN KEY (customer_id)
                                         REFERENCES public.customers(id)
                                         ON DELETE CASCADE
);

-- Many quotes for a jobsite/customer
CREATE TABLE public.quotes (
                               id BIGSERIAL PRIMARY KEY,
                               jobsite_id BIGINT NOT NULL,
                               customer_id BIGINT NOT NULL,
                               email VARCHAR(255),
                               estimate BIGINT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_quotes_jobsite
                                   FOREIGN KEY (jobsite_id)
                                       REFERENCES public.jobsites(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_quotes_customer
                                   FOREIGN KEY (customer_id)
                                       REFERENCES public.customers(id)
                                       ON DELETE CASCADE
);

-- Orders need site and customer, but do not require a quote
CREATE TABLE public.orders (
                               id BIGSERIAL PRIMARY KEY,
                               jobsite_id BIGINT NOT NULL,
                               customer_id BIGINT NOT NULL,
                               quote_id BIGINT,
                               email VARCHAR(255),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_orders_jobsite
                                   FOREIGN KEY (jobsite_id)
                                       REFERENCES public.jobsites(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_orders_customer
                                   FOREIGN KEY (customer_id)
                                       REFERENCES public.customers(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_orders_quote
                                   FOREIGN KEY (quote_id)
                                       REFERENCES public.quotes(id)
                                       ON DELETE SET NULL
);