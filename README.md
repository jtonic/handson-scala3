# handson-scala3

## Scope

 A project to get acquainted with Scala3 and its ecosystem.

## TODO

- [x] mvn configuration
- [x] simple cats-effects example
- [x] simple http4s endpoint with cats effect example
- [ ] http4s endpoint example (GET)
  - [x] with JSON return type
  - [x] with a collection of items
  - [x] query params
  - [ ] path param
- [ ] http4s endpoint example (POST)
- [x] slick, postgres, cats effects
- [x] slf4j/logback/cats-slf4j logging
- [ ] simple http4s HTTP client example with cats effect example
- [ ] simple configuration
- [ ] simple database example
- [ ] certificates and HTTPs
- [ ] integrate the application into a sidecare

## Applications


### App One (REST endpoints)

- build and run the application

> mvn clean install exec:java#run-app

- test the application

  ```shell

  http "localhost:8080/keepalive"

  http "localhost:8080/movies?actor=Brad%20Pitt"

  http "localhost:8080/actor?actor=Brad%20Pitt"

  ```

### App Two (slick + postgres + cats effects)

- build and run the application

> clean package exec:java@run-slick

  **Notes**:
    - Start docker-compose before


## Theory

- What is Kleisli from http4s routes PoV
- What is partial function (in the context of http4s endpoints definitions)
- unapply method (applicability)
- more about semigroup - in the context of HttpRoutes
