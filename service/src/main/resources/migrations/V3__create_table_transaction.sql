CREATE TABLE IF NOT EXISTS public.transaction (
    transaction_id UUID PRIMARY KEY NOT NULL,
    amount DECIMAL NOT NULL,
    event_date TIMESTAMP NOT NULL,
    account_id UUID NOT NULL,
    operation_type_id UUID NOT NULL,

    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES public.account(account_id),
    CONSTRAINT fk_transaction_operation_type FOREIGN KEY (operation_type_id) REFERENCES public.operation_type(operation_type_id)
);