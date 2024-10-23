
# Gestionnaire de Fichiers en Commandes en Ligne avec Annotations

### Équipe
- Berhil, Ilyes, 21916240
- Bouabdallaoui, Zeyneb, 21904931
- Kbayli, Yanis, 22005217
- Mihet, Alexandre, 22005024
- Billal, Medour, 21924103

## Description du projet
Ce mini-projet consiste à développer un gestionnaire de fichiers en ligne de commande qui s'appuie sur le système de fichiers de votre OS. Il permet d'annoter les éléments dans un répertoire (fichiers, répertoires).

## Contraintes
### Important
- Le projet doit être réalisé en Java par des groupes de plusieurs étudiants utilisant un dépôt Git commun.
- Une documentation complète doit être incluse dans le dépôt, sous forme de fichier `.md` ou `.adoc`, et doit contenir un manuel utilisateur et un manuel technique.
- Utilisation de `git` avec des commits réguliers et des messages informatifs. L'utilisation de "pull requests" est conseillée.
- Le build doit être géré par Maven (Maven wrapper inclus).
- Utilisation de Java version 17.
- Intégration de `checkstyle` pour vérifier le respect des règles de codage Google.
- Des tests unitaires avec JUnit 5 (version 5.9.1) doivent être présents pour la majorité des méthodes.
- Un outil de couverture de code doit être intégré au build.
- Les fonctionnalités de Java (POO, exceptions, collections, I/O) doivent être bien utilisées.
- L'application doit être exécutable à partir d'un `.jar` incluant toutes les dépendances.

## Fonctionnalités du gestionnaire de fichiers
- Interface textuelle affichant le contenu d'un répertoire avec un numéro d'élément de répertoire (NER) pour chaque fichier.
- Commandes utilisateur sous la forme `[<NER>] [<commande>] [<nom>]` (exemples : `3 cut`, `3`).
- Commandes principales à implémenter :
  - `[<NER>] copy`
  - `paste`
  - `[<NER>] cut`
  - `..` (remonter d'un cran)
  - `[<NER>] .` (entrer dans un répertoire)
  - `mkdir <nom>` (créer un répertoire)
  - `[<NER>] visu` (voir le contenu d'un fichier texte ou sa taille)
  - `find <nom fichier>` (recherche de fichiers dans les sous-répertoires)

- Annotation d'un élément : ajout ou retrait de texte associé (exemples : `3 + "ceci est un texte"`, `3 -` pour retirer l'annotation).

## Manuel utilisateur
### Commandes disponibles
- Une fonctionnalité 'help' liste les commandes et leur syntaxe lors de son exécution.
- Le fichier des annotations est mis à jour automatiquement lors de l'ajout ou de la suppression d'annotations.

### Suggestions d'amélioration
- Auto-complétion et suggestions pour les commandes.
- Historique des commandes pour faciliter la réutilisation.
- Raccourcis clavier pour les commandes fréquemment utilisées.
- Mode plein écran et personnalisation de l'interface utilisateur.

## Manuel technique
### Compiler le projet
**Sous Linux**
```bash
$ ./mvnw package
```

**Sous Windows**
```bash
> mvnw.cmd package
```

### Exécuter l'application
```bash
$ mvn clean compile assembly:single
$ java -jar ./target/explorer-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Vérifier les erreurs de checkstyle
```bash
$ mvn checkstyle:check
```

### Consulter le rapport de couverture de code
Exécuter la commande `$ mvn test`. Le rapport se trouve dans `target/site/jacoco/index.html`.

## Bibliothèques utilisées
- `java.time.LocalDateTime` : gestion des dates et heures.
- `java.util` : manipulation de collections de données.
- `java.io.File` : interaction avec le système de fichiers.
- `org.apache.commons.io` : gestion simplifiée des noms de fichiers.
- `com.google.gson` : sérialisation/désérialisation d'objets en JSON.
- `org.jline` : interface utilisateur en ligne de commande interactive.
- `org.junit.jupiter` : assertions dans les tests unitaires.

## Classes principales
- **Entite** : super classe définissant les attributs communs et les méthodes (`rm`, `copy`, `paste`, `find`).
- **Fichier** : gestion des objets Fichier et création sur disque si nécessaire.
- **Dossier** : gestion des objets Dossier avec méthodes pour vérifier l'existence.
- **Systeme_Fichier** : interactions avec le système de fichiers et navigation.
- **Annotation** : gestion des annotations avec sérialisation JSON.
- **Command** : lien entre l'utilisateur et les méthodes des autres classes.

## Évolutions possibles
- Intégration d'un système de droits d'utilisateur.
- Ajout de fonctionnalités pour déplacer ou renommer des fichiers/dossiers.
