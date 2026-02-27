DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'auth_provider') THEN
        CREATE TYPE auth_provider AS ENUM (
            'PASSWORD',
            'OIDC_GOOGLE'
        );
    END IF;
END
$$;

ALTER TABLE IF EXISTS public.collaborator
ADD COLUMN IF NOT EXISTS auth_providers auth_provider[];

UPDATE public.collaborator
SET auth_providers = ARRAY['PASSWORD']::auth_provider[]
WHERE password IS NOT NULL;
