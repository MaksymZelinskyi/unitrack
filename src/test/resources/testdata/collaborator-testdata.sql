INSERT INTO public.workspace(id, name)
VALUES (1, "Admin's workspace")
ON CONFILCT(id) DO NOTHING;

INSERT INTO public.collaborator(id, email, first_name, last_name, workspace_id, is_admin)
VALUES (1, 'testadmin@email.com', 'Admin', 'Test', 1, true);

UPDATE public.workspace
SET admin_id = 1
WHERE id = 1