-- -*- mode: sql; sql-product: sqlite; -*-

-- We want to track all function calls inputs and outputs.

BEGIN;

-- Is this a mess, or is it OK?  Its basically going to chain with
-- each node requiring an extra column.

CREATE TABLE det (
    identity PRIMARY KEY
  , input
  , output
  , date
);

INSERT INTO det (identity, input, output, date)
VALUES
  ('hello_world', '', 'Hello World', '2019-01-01 12:00:00')
, ('bye_world', '', 'Goodbye World', '2019-01-01 12:00:00')
;

-- Pretty print
.headers on
.mode column

select * from det;

COMMIT;
