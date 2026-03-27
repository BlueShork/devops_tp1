# DevOps TP1

Un projet Spring Boot simple pour la pratique DevOps.

## Prérequis

- Java 17 ou supérieur
- Gradle 7+

## Installation

```bash
git clone git@github.com:BlueShork/devops_tp1.git
cd devops_tp1
```

## Utilisation

### Compiler le projet

```bash
./gradlew build
```

### Exécuter les tests

```bash
./gradlew test
```

### Lancer l'application

```bash
./gradlew bootRun
```

## CI/CD

Une pipeline GitHub Actions est configurée pour valider automatiquement chaque Pull Request vers `main`.
