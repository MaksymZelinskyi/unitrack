ALTER TABLE IF EXISTS public.project
    ADD COLUMN completed boolean;

UPDATE public.project p
SET completed = CASE
    WHEN p.status = 2 THEN true
    ELSE false
END;

ALTER TABLE IF EXISTS public.project
    DROP COLUMN status;