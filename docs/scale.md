# scale.md

This document explains how the **MovieRating** service can be scaled to maintain performance and reliability as usage grows from hundreds to millions of daily users.

## Expected Growth

The system is designed to handle growth from:

* **\~100 users/day (dev/test traffic)**
  → to
* **\~10,000,000 users/day (production at global scale)**

## Cloud-Native Scalability

* The application is **fully containerized** and deployable on any cloud-native platform such as **Kubernetes**, **AKS**, **EKS**, or **GKE**.
* All major cloud providers (AWS, Azure, GCP) support **auto-scaling of pods and nodes** via horizontal/vertical pod autoscalers (HPA/VPA).
* **Helm charts** can be used to declare resource limits, replicas, affinity rules, and scale-out thresholds as part of CI/CD workflows.

## Runtime-Level Optimization (Java 21)

If the platform allows the use of **Java 21**, the system can benefit significantly from:

* **Virtual Threads (Project Loom)**
  Java 21's lightweight virtual threads allow for massive concurrency with minimal thread overhead, perfect for I/O-bound APIs such as:

    * OMDb metadata fetching (if not only used at start-up)
    * JWT token validation
    * DB access for ratings and movie lookups

> This reduces thread pool contention and makes the app more memory- and CPU-efficient under high load.

## Application Layer Scaling

* **Stateless Spring Boot microservice**
  Designed to be horizontally scalable; no sticky sessions or in-memory state

* **Database access** is abstracted via JPA, and PostgreSQL can be:

    * Read-scaled using **replicas**
    * Write-scaled using **sharding** or **partitioning** (if needed at extreme scale)

* **Rate limiting** and **caching layers** (e.g., Redis) can be introduced to protect hot endpoints like:

    * `/top-10-rated-movies`
    * `/ratings`

## Observability & Autoscaling

The application exposes standard **Spring Boot Actuator** endpoints which can be scraped by tools like:

* **Prometheus** + **Grafana** for metrics-based autoscaling
* **KEDA** for Kubernetes event-driven autoscaling (e.g., based on queue depth or HTTP traffic)
* **OpenTelemetry** for distributed tracing

## Security and Gateway Considerations

* OAuth2 / Entra ID-based authentication scales well under load, but token validation and introspection can be offloaded to an **API Gateway** or **Sidecar Auth Proxy** (e.g., Envoy with OPA).

## Future Enhancements for Scale

If traffic increases significantly, consider:

* **Async rating ingestion** via message queues (Kafka/SQS)
* **CQRS** pattern for read/write separation of rating data
* **Pre-aggregated views** or materialized views for faster leaderboard queries
