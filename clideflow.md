# Passation — Spring Boot Packaging

## Ce qui a été fait
- Projet Spring Boot créé avec Gradle (structure standard)
- Gradle Wrapper 8.10 généré (compatible Java 23)
- Spring Boot 3.4.3 + dependency-management 1.1.7
- Build réussi : `./gradlew build`
- JAR exécutable testé : `java -jar build/libs/devops-tp1-0.0.1-SNAPSHOT.jar` → démarre sur port 8080

## Fichiers créés/modifiés
- `build.gradle` — config Gradle avec plugins Spring Boot
- `settings.gradle` — nom du projet
- `gradlew` / `gradlew.bat` — Gradle wrapper scripts
- `gradle/wrapper/gradle-wrapper.properties` — Gradle 8.10
- `gradle/wrapper/gradle-wrapper.jar` — binaire du wrapper
- `src/main/java/fr/devops/devops_tp1/DevopsTp1Application.java` — point d'entrée
- `src/main/resources/application.properties` — config Spring
- `src/test/java/fr/devops/devops_tp1/DevopsTp1ApplicationTests.java` — test de contexte

## Décisions
- Gradle 8.10 choisi car Java 23 installé (8.5 incompatible avec Java 23)
- Spring Boot 3.4.3 (dernière stable)
- Java sourceCompatibility = 23

## Pour le prochain agent
- Le projet compile et les tests passent
- L'app démarre mais n'a aucun endpoint (retourne 404)
- Commandes : `./gradlew build` pour builder, `java -jar build/libs/devops-tp1-0.0.1-SNAPSHOT.jar` pour lancer
