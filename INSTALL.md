# UniTrack - Guide d'installation et de déploiement

## 1\. Prérequis
- Docker + Docker Compose v2
- Git

Docker s'occupe de toutes les dépendances internes
## 2\. Installation locale

```bash
git clone https://github.com/MaksymZelinskyi/unitrack.git
cd unitrack
cp .env.example .env
```

Éditez `.env` et renseignez toutes les variables (voir section suivante).

## 3. Variables d'environnement (`.env`)

| Variable | Description                                                        | Exemple          |
|---|--------------------------------------------------------------------|------------------|
| `POSTGRES_USERNAME` | Utilisateur PostgreSQL et rôle Flyway                              | `unitrack-admin` |
| `POSTGRES_PASSWORD` | Mot de passe associé                                               | password         |
| `POSTGRES_DB` | Nom de la base de données                                          | unitrack-db      |
| `SMTP_EMAIL` / `SMTP_PASSWORD` | Compte d'envoi d'e-mails(pour la réinitialisation de mot de passe) | —                |
| `GOOGLE_OAUTH2_CLIENT_ID` / `_SECRET` | OAuth2 Google                                                      | —                |


## 4. Lancement en local
Depuis la racine du répertoire exécutez
```bash
docker compose up
```
Pour arrêter l'application exécutez 
```bash
docker compose down
```
Pour réinitializer la base de données supprimez le volume
```bash
docker volume rm unitrack_postgres_data
```
Pour appliquer des modifications de code recréez les conteneurs
```bash
docker compose up --build --force-recreate
```
