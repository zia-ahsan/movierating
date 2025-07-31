# solution.md

The **MovieRating** application is a full-stack, production-ready Java REST API and UI (Spring MVC) that allows users to rate movies, check whether a movie won the Oscar for Best Picture, and view a list of top 10 highest-rated movies sorted by box office revenue. The list of movies extracted from a CSV file (src/main/resources/data/academy_awards.csv). The movie information gets enriched by calling the public OMDb API such as box office value.

The backend is built using **Spring Boot (Java 17)**, with **PostgreSQL** for persistence, **JWT-based authentication** via **Microsoft Entra External ID**, and a unified REST/Thymeleaf interface. Key design decisions include:

* A modular architecture with clear separation between controller, service, and persistence layers
* Exception handling with custom exception types and global error responses
* Stateless, token-based security for scalable, decoupled authentication
* Caching of Oscar CSV data for performance
* OpenAPI documentation for exposing and testing the REST APIs
* Test coverage with JUnit 5, Mockito, and integration tests via MockMvc
* Please check the other docs and all the diagrams in /diagrams

This solution is designed to be clean, maintainable, and extensible while satisfying all business requirements specified in the MyOrg challenge.

