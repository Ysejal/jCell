# jCell

Implémentations JavaFX de divers automates cellulaires.

## Prerequisites

- SDKMAN!
- Java 11
- JavaFX SDK 11
- Maven

Le projet a été testé sur Archlinux avec la version 5.4 du noyau linux et sur l'installation de Windows 10.

## Installation

SDKMAN ! est un outil permettant de gérer les versions parallèles de plusieurs kits de développement logiciel Java sur la plupart des systèmes basés sur Unix.
Il suffit d'ouvrir un nouveau terminal et d'entrer :

```bash
curl -s "https://get.sdkman.io" | bash
```

Suivez les instructions à l'écran pour terminer l'installation.
Ensuite, ouvrez un nouveau terminal **ou** entrez :

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Pour le Java JDK, il y a maintenant plusieurs fournisseurs. Donc, en plus de la version, vous devez également spécifier un fournisseur :

```bash
sdk install java 11.0.7-zulu
```

Maven peut gérer la construction, le reporting et la documentation du projet à partir du POM.xml :

```bash
sdk install maven
```

## Running

La compilation du projet est gérée par Maven. Pour compiler le projet et l'exécuter, exécutez :

```bash
mvn javafx:run
```
