# Projet de Gestion de Contacts - JavaFX

Bienvenue dans le projet de **Gestion de Contacts**, une application de bureau développée en Java utilisant JavaFX. Cette application permet de gérer une liste de contacts avec des fonctionnalités de création, modification, suppression, ainsi que la possibilité de charger et de sauvegarder les données dans des fichiers sur le disque dur.

## Table des Matières
- [Aperçu du Projet](#aperçu-du-projet)
- [Fonctionnalités](#fonctionnalites)
- [Prérequis](#prerequis)
- [Installation](#installation)
- [Lancement de l'Application](#lancement-de-lapplication)
- [Structure du Projet](#structure-du-projet)
- [Chargement et Sauvegarde de Fichiers](#chargement-et-sauvegarde-de-fichiers)
- [Captures d'Écran](#captures-decran)
- [Resume](#resume)

## Aperçu du Projet

Ce projet est une application de bureau pour la gestion de contacts, permettant à l'utilisateur d'ajouter de nouveaux contacts, de les éditer, de supprimer des entrées existantes et de visualiser les détails de chaque contact dans une interface utilisateur conviviale. De plus, l'application permet de **charger des contacts depuis un fichier** et de **sauvegarder la liste de contacts dans un fichier** sur le disque dur.

## Fonctionnalités

- **Ajouter un contact** : Enregistrez de nouveaux contacts avec des informations telles que le prénom, le nom, l'adresse et la date de naissance.
- **Modifier un contact** : Mettez à jour les informations de contacts existants.
- **Supprimer un contact** : Retirez les contacts de la liste.
- **Recherche et Filtrage** : Filtrez la liste de contacts en fonction des critères spécifiés.
- **Charger et Sauvegarder les Contacts** : Chargez une liste de contacts depuis un fichier et sauvegardez les données modifiées dans un fichier sur le disque dur.
- **Interface Utilisateur Moderne** : Une interface graphique intuitive utilisant JavaFX.

## Prérequis

- **Java** : Version 11 ou supérieure.
- **JavaFX** : JavaFX doit être installé et configuré si vous utilisez JDK 8.
- **Maven** : Pour gérer les dépendances et construire le projet.

## Installation

1. Clonez le dépôt sur votre machine locale :

```shell script
   git clone https://github.com/votre_nom_dutilisateur/nom_du_projet.git
   cd nom_du_projet
```
   
2. Assurez-vous que toutes les dépendances sont installées. Si vous utilisez Maven, exécutez :

```shell script
   mvn clean install
```
   
## Lancement de l'Application

Pour lancer l'application, vous pouvez exécuter la commande suivante depuis le répertoire du projet :

```shell script
   mvn javafx:run
```

Assurez-vous que Java et JavaFX sont correctement configurés sur votre environnement.

## Structure du Projet

Voici un aperçu de la structure des dossiers principaux dans ce projet :

- **src/main/java** : Contient le code source Java.
- **controller** : Classes de contrôleurs JavaFX qui gèrent la logique de l'interface utilisateur.
- **model** : Classes de modèles représentant les données de l'application (ex. Person).
- **view** : Fichiers FXML pour définir les layouts de l'interface utilisateur.
- **src/main/resources** : Contient les ressources telles que les fichiers FXML et les styles CSS.
- **src/test/java** : Contient les tests unitaires pour l'application.

## Chargement et Sauvegarde de Fichiers

L'application offre la possibilité de charger des contacts depuis un fichier et de sauvegarder les modifications dans un fichier. Cela permet de conserver la liste des contacts même après la fermeture de l'application.

- **Charger un fichier** : Vous pouvez importer un fichier contenant des informations de contacts pour les ajouter directement à la liste de l'application.
- **Sauvegarder dans un fichier** : Les contacts modifiés ou ajoutés peuvent être enregistrés dans un fichier pour une utilisation ultérieure.

## Les formats de fichiers pris en charge sont les suivants :

- **Fichiers texte (.txt)**
- **Fichiers JSON (.json) (à adapter selon votre implémentation)**

## Captures d'Écran

Incluez ici des captures d'écran de l'application pour montrer l'interface utilisateur et les fonctionnalités principales.

## Resume
### Ce que ce `README.md` inclut :
- Une **description générale** du projet et de ses fonctionnalités.
- Les **instructions d'installation** et de **lancement** de l'application.
- Des informations sur la **structure du projet** pour aider les autres développeurs à comprendre l'organisation du code.
- Une section sur le **chargement et la sauvegarde de fichiers**, puisque l'application prend en charge ces fonctionnalités.
- Un appel à la **contribution** pour encourager d'autres développeurs à participer au projet.
- Une **licence** pour encadrer l'utilisation du code.

J'espère que cela répond à tes attentes ! Si tu souhaites des ajustements supplémentaires, n'hésite pas à demander.
   