# Interview Test App

Minimal API test suite for the Player service (Java + Maven). Tests use TestNG, RestAssured and Allure for reporting.

## Tech stack
- Java (JDK 11+)
- Maven
- TestNG
- RestAssured
- Allure (attachments & reporting)
- SLF4J for logging

## Project layout (high level)
- `src/main/java` — application/test framework code (services, DTOs, utils, config)
- `src/test/java` — tests (TestNG tests)
- `src/main/resources/config.properties` — runtime configuration (base URL)
- `src/test/resources/testng.xml` — TestNG suite configuration
- `target/` — build and test outputs

## Configuration
Edit the API base URL in:
- `src/main/resources/config.properties` (property `base.url`)

Config is loaded via `config.ConfigManager` and validated at startup.

## Run tests
- In IDE: run TestNG suites or individual tests (IntelliJ IDEA recommended).
- From command line (run all tests):
    - `mvn clean test`
- Run a single test class or method:
    - `mvn -Dtest=player.PlayerCreateTests test`
    - `mvn -Dtest=player.PlayerCreateTests#createPlayerWithValidRequest test`

## Allure report
If Allure CLI/plugin is available:
- After tests: `allure serve allure-results`

## Known bugs
- BUG-1: Response returns null values for created player DTOs (reported in create tests).
- BUG-2: Player with invalid data is created instead of returning 400.
- BUG-3: Creating a player as a non-existing Admin returns 403; test expects 401 (authorization ambiguity).
- BUG-4: Deleting as a non-existing Admin returns 403; expected 401.
- BUG-5: Deleting a non-existing player returns 403; expected 204 or 404 (idempotence/contract mismatch).
- BUG-6: User role is able to delete players though this operation should be forbidden for User role.
- BUG-7: `getAll` response is missing the `role` property.
- BUG-8: Getting a deleted player returns 200 with an empty body; expected 404.
- BUG-9: Updating a non-existing player returns 200 (success) instead of 404.
- BUG-10: Player can be updated with invalid data; 400 should be returned but API accepts it.
- BUG-11: User role is able to update another player (should be forbidden; test expects 403).

## Clarifications / Open questions
- Authorization: should requests from invalid credentials return 401 (unauthorized) or 403 (forbidden)? Several tests expect 401 but API returns 403.
- Delete semantics: should deleting an already-deleted/non-existing resource be idempotent returning 204, return 404, or something else?
- Create-existing behavior: when creating a player with existing login/screenName, what is the expected response code and behavior (409/400/200)?
- Password: should password be present in update/get responses — `PlayerUpdateTests` notes response doesn't return `password` property; confirm expected behavior for security and tests.