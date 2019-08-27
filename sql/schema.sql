-- -*- mode: sql; sql-product: sqlite; -*-

-- We want to track all function calls inputs and outputs.

BEGIN;

-- Is this a mess, or is it OK?  Its basically going to chain with
-- each node requiring an extra column.

CREATE TABLE det (
    identity
  , input
  , input_types
  , output
  , output_type
  , date
);

INSERT INTO det (identity, input, input_types, output, output_type, date)
VALUES
  ('hello', 'Matt', 'string', 'Hello Matt', 'string', '2019-01-01 12:00:00')
, ('bye', '42', 'integer', 'Bye 42', 'string', '2019-01-01 12:00:00')
;

-- Pretty print
.headers on
.mode column

select * from det;

COMMIT;
