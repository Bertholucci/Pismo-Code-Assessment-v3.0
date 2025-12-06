CREATE TABLE IF NOT EXISTS public.operation_type(
    operation_type_id uuid NOT NULL PRIMARY KEY,
	description TEXT NOT NULL,
	charge_type TEXT NOT NULL,
    CONSTRAINT charge_type_check CHECK (charge_type IN ('CREDIT', 'DEBIT'))
);