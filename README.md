![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white)
![Angular](https://img.shields.io/badge/angular-%23DD0031.svg?style=for-the-badge&logo=angular&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

<br/>
![Static Badge](https://img.shields.io/badge/18.0.3-Angular_version-red)
![Static Badge](https://img.shields.io/badge/11-JAVA_version-orange)
![Static Badge](https://img.shields.io/badge/3.3.4-Spring_Boot_version-gree)

# 🪳 Projet 4 - Testez une application full-stack

L'objectif de ce projet est de tester une application Fullstack.

## 📖 Sommaire

- [Projet](#-projet)
    - [Prérequis](#prérequis)
    - [Importer la base de données MySQL](#importer-la-base-de-donnée-mysql)
    - [Variables d'environnement](#variable-denvironnement)
    - [Build le projet](#build-le-projet)
    - [Build l'artifact](#build-lartifact)
- [Etape de conception](#-etape-de-conception)
- 
## 📁 Projet

### Prérequis

1. Fork ou cloner ce projet
2. Installer [MySQL 8 command Line](https://openclassrooms.com/fr/courses/6971126-implementez-vos-bases-de-donnees-relationnelles-avec-sql/7152681-installez-le-sgbd-mysql) ou avec [Docker](https://spring.io/guides/gs/accessing-data-mysql)
3. [Importer la BDD MySQL](#importer-la-base-de-donnée-mysql)
4. Installer les [Variables d'environnement](#variable-denvironnement)

## Front-end

### Démarrer le projet

1. Faire `ng serve` pour démarrer le serveur de développement.
2. Aller sur `http://localhost:4200/`.

## Back-end

### Importer la base de donnée MySQL

Dans le fichier `resources` à la racine du projet puis dans `mysql` vous trouverez un `script.sql` à importer dans votre MySQL une fois installé

Si vous n'êtes pas connecté :

`mysql -u root -p nom_de_la_base_de_donnees < mon/chemin/resources/mysql/script.sql;`

Si vous êtes connecté :

`use nom_de_la_base_de_donnees;`

`source mon/chemin/resources/mysql/script.sql;`

### Variable d'environnement

Configurer les variables d'environnement directement sur Intellij:

`Run` > `Edit Configuration` > `Modify options` > cocher `Variable environment` >
Dans le champ `Environment variables` cliquer sur `$`

Liste des variables d'environnement :

```
DATABASE_USERNAME -> identifiant_mysql
DATABASE_PASSWORD -> mdp_mysql
```
> [!TIP]
> Vous pouvez générer votre JWT_SECRET_KEY avec la commande:
> `openssl rand -base64 32`

### Build le projet

1. Cliquer sur le bouton **play** `Run 'SpringBootSecurityJwtApplication'` dans la barre en haut d'**IntelliJ**.
2. Soit, vous faites des requêtes depuis postman `resources/postman` ou cloner et démarrer le front-end.

## ⚙️ Etape de test



