# how_to_run.md

To run the **MovieRating** application locally, follow the steps below:

#### Prerequisites

* **Java 17+**
* **Maven 3.x** (used 3.9.10)
* **PostgreSQL** (recommended version 14+, used 17.5-2-windows-x64)
* **Internet access** (for OMDb API calls and dependency downloads)
* **Microsoft Entra External ID (formerly Azure AD B2C)** configuration with:

    * Registered client application (registered as MovieRatingApp, MovieRatingAppClient)
    * Valid client-id, client-secret, redirect-uri, issuer-uri, redirect-uri, policy, logout-redirect-uri (provided in application.yml)

#### 🚀 Steps to Run Locally

1. **Unzip the provided source code movierating.zip**

   ```bash
   cd path/movierating
   ```

2. **Start PostgreSQL** and create the database:

   ```sql
   CREATE DATABASE movierating;
   ```
3. **Update the application.properties** with your db name in url, username, password and omdb.api.key

      ```properties
      spring.datasource.url=jdbc:postgresql://localhost:5432/movierating
      spring.datasource.username=myorg_user
      spring.datasource.password=password123
      omdb.api.key=ac0264bb
      ```
4. **Run the application**

   ```bash
   mvn spring-boot:run
   ```
5. **Access the app**

    * UI: [https://localhost:8443](http://localhost:8443)
    * Swagger: [http://localhost:8080/swagger-ui.html](https://localhost:8443/swagger-ui.html)
    * API base: [http://localhost:8080/api/v1/movies](http://localhost:8080/api/v1/movies)


