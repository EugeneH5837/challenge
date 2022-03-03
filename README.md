## README for Pokemon Challenge

### Instructions for running
Application is built on jvm version 11, ensure you have at least version 11 installed locally.

To start off, inside main directory of project first build using gradle.

```./gradlew clean build```

Then to run the application either use run command in an IDE like IntelliJ or Eclipse, or run command in same directory as above.

```./gradlew bootrun```

The pokemon data is stored in a json file under `src/main/resources/data/pokemon.json`. To load the data from the json file into the h2 database, hit the endpoint while the service is running under `localhost:8080/api/v1/pokemon/loaddata`.

To update the path to a new data file location, update the value in `application.yaml` for `data.path`.

### Endpoints

While application is running, go to `localhost:8080/swagger-ui.html` to view swagger page for endpoints.

Additional under `/postmancollection` is a postman collection you may import to test the various endpoints.