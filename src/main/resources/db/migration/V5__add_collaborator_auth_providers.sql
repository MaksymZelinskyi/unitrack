CREATE TABLE IF NOT EXISTS public.collaborator_auth_providers (
    collaborator_id bigint NOT NULL REFERENCES public.collaborator(id),
    auth_providers varchar(255) NOT NULL
);