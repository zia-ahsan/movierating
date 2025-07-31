# to_do.md

This document outlines items that are currently pending or could be added to improve the MovieRating API project further.


## High Priority Improvements

* **Microservice Architecture:**
  Separate UI from backend and deploy in Azure cloud as microservices.
* **Better Test Coverage:**
  Add complete unit and integration tests for edge cases, error scenarios, and security enforcement with jacoco code coverage and enforcing a certain percentage to pass the build.

* **Fix Swagger UI with OAuth2:**
  Resolve the `NoSuchMethodError` caused by conflicting Spring dependencies to allow OAuth2 `Authorize` button in Swagger UI.

* **Centralized Exception Handling:**
  Introduce a `@ControllerAdvice` based global exception handler with structured error responses and status codes.

* **Logging and Monitoring:**
  Integrate proper structured logging (e.g., Logback, JSON logs) and monitoring tools (like Prometheus/Grafana or Micrometer).

* **Deploy in Azure cloud with Azure Repos, CI/CD Pipelines, AKS:**
  Initial goal was to deploy the application on Azure Cloud using Azure Repos, CI/CD, and AKS under a “Pay-As-You-Go” subscription and expose with public access. However, the tenant couldn’t be linked to a billing account, which blocked the deployment process and delayed my submission as well.  
  While exploring alternative platforms, was able to deploy a working version (not completed) here for review:
  - Application : https://movierating-production.up.railway.app/
  - OpenAPI (Swagger) : https://movierating-production.up.railway.app/swagger-ui/index.html

* **OpenTelemetry Integration:** Trace distributed flow and improve observability using OpenTelemetry.

## Medium Priority Enhancements

* **Role-Based Access Control (RBAC):**
  Introduce support for roles like `USER`, `ADMIN`, etc., and secure endpoints accordingly.

* **Rate Limiting:**
  Implement basic rate-limiting to protect endpoints against abuse.

* **Validation Enhancements:**
  Improve input validation, especially for rating values and movie IDs, using `@Valid` and custom validators.
