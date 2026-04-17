# Spec — Intégration JaCoCo + SonarCloud

**Date** : 2026-04-17
**Projet** : DEVOPS_TP1 (Spring Boot 2.7.18, Gradle)
**Référence** : README de https://github.com/benoit-charroux/rent

## Objectif

Ajouter au projet :
1. **Couverture de code** via JaCoCo (rapport HTML + XML, seuil indicatif 80%)
2. **Contrôle qualité** via SonarCloud (analyse statique, code smells, vulnérabilités, bugs)

Les deux doivent être exécutés dans le pipeline CI GitHub Actions sur chaque PR et chaque push sur `main`.

## Périmètre

**Inclus**
- Ajout du plugin JaCoCo à `build.gradle`
- Ajout du plugin `org.sonarqube` à `build.gradle`
- Fichier `sonar-project.properties` (ou équivalent dans `build.gradle`)
- Extension du workflow `.github/workflows/ci.yml`
- Documentation du setup manuel côté SonarCloud (README ou note)

**Exclus**
- Blocage du build sur seuil de couverture (seuil **indicatif** uniquement)
- Écriture de nouveaux tests (l'existant JUnit est la base)
- Migration Java 1.8 → 17 (déjà incohérent entre `build.gradle` et CI, mais hors sujet)
- Tests MockMvc

## Architecture

### 1. JaCoCo — `build.gradle`

```gradle
plugins {
    id 'java'
    id 'jacoco'
    id 'org.springframework.boot' version '2.7.18'
    id 'io.spring.dependency-management' version '1.1.4'
}

jacoco {
    toolVersion = "0.8.11"
}

test {
    finalizedBy jacocoTestReport
}

jacocoTestReport {
    dependsOn test
    reports {
        xml.required = true
        html.required = true
    }
}
```

**Sorties** :
- HTML : `build/reports/jacoco/test/html/index.html`
- XML : `build/reports/jacoco/test/jacocoTestReport.xml` (consommé par SonarCloud)

**Seuil** : 80% indicatif — aucune tâche `jacocoTestCoverageVerification` n'est activée pour bloquer le build.

### 2. SonarCloud — `build.gradle` + `sonar-project.properties`

**`build.gradle`** (ajout du plugin) :

```gradle
plugins {
    // ... plugins existants
    id 'org.sonarqube' version '4.4.1.3373'
}
```

**`sonar-project.properties`** (à la racine) :

```properties
sonar.projectKey=BlueShork_devops_tp1
sonar.organization=<ORG_KEY>
sonar.host.url=https://sonarcloud.io
sonar.java.binaries=build/classes
sonar.coverage.jacoco.xmlReportPaths=build/reports/jacoco/test/jacocoTestReport.xml
```

`<ORG_KEY>` à remplir une fois l'organisation SonarCloud créée (probablement `blueshork`).

### 3. CI — `.github/workflows/ci.yml` étendu

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repository
        uses: actions/checkout@v4
        with:
          fetch-depth: 0  # requis par SonarCloud

      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cache SonarCloud packages
        uses: actions/cache@v4
        with:
          path: ~/.sonar/cache
          key: ${{ runner.os }}-sonar
          restore-keys: ${{ runner.os }}-sonar

      - name: Cache Gradle packages
        uses: actions/cache@v4
        with:
          path: ~/.gradle/caches
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle') }}
          restore-keys: ${{ runner.os }}-gradle

      - name: Build and test with coverage
        run: ./gradlew build jacocoTestReport

      - name: SonarCloud Scan
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: ./gradlew sonar
```

### 4. Setup manuel SonarCloud (à faire par l'utilisateur après intégration)

1. Se connecter à https://sonarcloud.io avec le compte GitHub
2. Créer/rejoindre l'organisation (noter la clé `<ORG_KEY>`)
3. Importer le projet `DEVOPS_TP1` (projectKey attendu : `BlueShork_devops_tp1`)
4. Choisir le mode d'analyse **"With GitHub Actions"** (désactive l'Automatic Analysis)
5. Générer un token SonarCloud
6. Ajouter le token dans `GitHub → Settings → Secrets → SONAR_TOKEN`
7. Mettre à jour `sonar.organization` dans `sonar-project.properties`

## Flux de données

```
./gradlew build jacocoTestReport
        │
        ├─► build/classes/…                    (compilation Java)
        ├─► build/reports/tests/test/          (rapports JUnit HTML)
        └─► build/reports/jacoco/test/
                 ├─► html/index.html           (lecture humaine)
                 └─► jacocoTestReport.xml ─────┐
                                               │
./gradlew sonar ◄──────────────────────────────┘
        │
        └─► SonarCloud (projet BlueShork_devops_tp1)
                 ├─► Coverage (%)
                 ├─► Bugs / Vulnerabilities / Code Smells
                 └─► Quality Gate status (affiché dans les PRs)
```

## Gestion des erreurs

| Scénario | Comportement attendu |
|---|---|
| `SONAR_TOKEN` absent/invalide | Étape `sonar` échoue, le reste du build a déjà passé → rapport JaCoCo quand même disponible en artifact local |
| Tests échouent | `gradlew build` échoue → JaCoCo et Sonar non exécutés (ordre naturel) |
| `fetch-depth: 0` oublié | SonarCloud warn sur la détection de blame/branches mais l'analyse tourne |
| Couverture < 80% | Rapport affiche le chiffre, **aucun blocage** (seuil indicatif) |

## Critères d'acceptation

- [ ] `./gradlew test jacocoTestReport` génère `build/reports/jacoco/test/jacocoTestReport.xml`
- [ ] Rapport HTML JaCoCo ouvrable localement
- [ ] `sonar-project.properties` présent et référence le projectKey correct
- [ ] Workflow CI passe sur PR et push main (modulo SONAR_TOKEN à configurer)
- [ ] Une fois `SONAR_TOKEN` ajouté : résultat SonarCloud visible sur https://sonarcloud.io/project/overview?id=BlueShork_devops_tp1
- [ ] Badge/statut SonarCloud remonté dans les PRs GitHub
