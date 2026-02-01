INSERT INTO public.workspace(id, name)
VALUES (1, 'Admin''s workspace');

INSERT INTO public.collaborator(id, email, first_name, last_name, workspace_id, is_admin)
VALUES (1, 'testadmin@email.com', 'Admin', 'Test', 1, true);

UPDATE public.workspace
SET admin_id = 1
WHERE id = 1;

INSERT INTO public.collaborator(id, email, first_name, last_name, workspace_id, is_admin)
VALUES (2, 'testuser@email.com', 'John', 'Doe', 1, false);

INSERT INTO public.project(id, title, workspace_id, completed, start, "end")
VALUES (1, 'Project1', 1, false, now(), '2027-12-31');

INSERT INTO public.participation(project_id, collaborator_id)
VALUES (1, 2);

INSERT INTO public.task(id, project_id, workspace_id, title, status, completed_on)
VALUES
 (1, 1, 1, 'Task1', 2, now()),
 (2, 1, 1, 'Task2', 2, now());

INSERT INTO public.task_assignees(task_id, assignees_id)
VALUES
 (1, 2),
 (2, 2);