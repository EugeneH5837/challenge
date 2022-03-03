## README for Pokemon Challenge

### Instructions for running
Application is built on jvm version 11, ensure you have at least version 11 installed locally.

To start off, inside main directory of project first build using gradle.

```./gradlew clean build```

Then to run the application either use run command in an IDE like IntelliJ or Eclipse, or run command in same directory as above.

```./gradlew bootrun```

The pokemon data is stored in a json file under `src/main/resources/data/pokemon.json`. To load the data from the json file into the h2 database, hit the endpoint while the service is running under `localhost:8080/api/v1/pokemon/loaddata`.

To update the path to a new data file location, update the value in `application.yaml` for `data.path`.

## Endpoints

While application is running, go to `localhost:8080/swagger-ui.html` to view swagger page for endpoints.

Additionaly under `/postmancollection` is a postman collection you may import to test the various endpoints.

### Endpoints Description

- GET `/api/v1/pokemon/{id}?language={language}`: Get a Pokemon by ID, optional request parameter is language.
If a value is passed in for language (english, japanese, chinese, french), the Pokemon is returned with
their name only in that language. If no value is provided, all names for the Pokemon in the four languages are provided.

- PUT `/api/v1/pokemon/{id}?caught={true/false}`: Update a Pokemon's caught status by ID by caught request parameter.

- PUT `/api/v1/pokemon/name/{name}?caught={true/false}`: Update a Pokemon's caught status by name. Name can be provided
in any language.

- GET `/api/v1/pokemon/types`: Get a list of all Pokemon types.

- GET `/api/v1/pokemon/list?type={type1,type2}&caught={true/false}&name={name}&language={language}&page={pageNum}&size={pageSize}`:
Get a list of all Pokemon based request parameters. All request params are optional, if none are passed in then a list of Pokemon
are retrieved starting from the 0th page with a default page size of 20, all names in all languages are returned.
  - Pokemon can be retrieved by one or more types, if more than one type is passed in, only Pokemon that have **all** types are returned. 
  - Caught filters based on whether Pokemon have been caught or not. 
  - Pokemon can be retrieved by a name in any language and will be returned. For english and french, input is case insensitive.
  - Language if provided without name parameter, returns all matching pokemon with name in specified language. If provided in conjunction with name
  parameter, then retrieves the matching Pokemon by the name provided, and gives the Pokemon with name in the language provided. If an unsupported language is passed through, then it will throw a 400.
  - Page defaults to 0th page, values can be passed in to move to corresponding page.
  - Size defaults to 20 elements, can be adjusted to more or less elements per page.

- GET `/api/v1/pokemon/loaddata`: Loads data from specified JSON file in path in `application.yaml`.