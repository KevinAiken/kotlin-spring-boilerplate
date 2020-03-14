# kotlin-spring-boilerplate

This is my personal boilerplate for Kotlin+Spring projects. 

It has some useful examples of various functionality, including REST API's, database usage, and serving static pages.

## Useful commands

Build the project (runs tests as well): `./gradlew build`

Run  the project: `./gradlew bootrun`

Run the tests (will create a temporary postgres database for integration testing): `./gradlew test`

Check for linter issues: `./gradlew ktlint`

Automatically apply correct formatting: `./gradlew ktlintFormat`

Migrate the database referenced in application.properties: `./gradle flywayMigrate`

## Todos

Figure out the best way to use a local test database

Add a socket example