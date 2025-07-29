INSERT INTO collaborator(is_admin, join_date, avatar_url, email, first_name, last_name, password)
SELECT true, NOW(), 'https://tse4.mm.bing.net/th/id/OIP.2ju3SjeaCC3oG4spkIrHCgHaI0?rs=1&pid=ImgDetMain&o=7&rm=3',
       'admin@email.com', 'Admin', 'Admineux',
       '$2a$10$qV0/2PAfwXf1J74Pjge9r.2KVQ8xr.1mx9mBU12k9K8sPTAL4DhZ6'
WHERE NOT EXISTS (
    SELECT 1 FROM collaborator WHERE email = 'admin@email.com'
);
