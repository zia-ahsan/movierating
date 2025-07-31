# assumptions.md

This document outlines the assumptions made during the development of the MovieRating API based on communication with the MyOrg team (via email discussion with Deke Barnett on June 26th).

## Oscar “Best Picture” Logic

* The **provided CSV file** is considered the **sole source of truth** for determining whether a movie won or was nominated for "Best Picture".
* The **OMDb API** is used *only* to supplement movie metadata such as **box office values**. It does **not** contain reliable information on Oscar wins, especially in the "Best Picture" category.
* As a result, the `/api/v1/movies/best-picture-winner/{movie-name}` endpoint is driven solely by the data from the CSV.

## User Ratings Scope

* Due to inconsistencies in the CSV—such as movie titles appearing in “Additional Info” fields for some categories (e.g., "Actor -- Leading Role")—only movies explicitly listed in the **"Best Picture"** category are available for **user rating**.
* This avoids mismatched titles and ensures accurate data correlation.

## Top 10 Movies Logic

* The `/api/v1/movies/top-10-rated-movies` endpoint returns:

  > provide a list of 10 top-rated movies ordered by box office value
* This means:

    1. Select **top 10 highest-rated** movies.
    2. Then sort that result by **box office** revenue.
* This approach is confirmed by the MyOrg team by mentioning these assumptions in this document.
* Another assumption is that one single user can have only one rating for a particular movie but can modified and the last rating will be persisted. This behavior can be verified by the UI that allows self-service user creation and login/logout with Microsoft Entra External ID.


## Data Preloading

* On application startup, the backend **parses and imports** relevant movie data from the provided CSV into the **relational database**.
* This ensures that:
    * All "Best Picture" winners are persisted for querying.
    * The system is testable and functional even **without external API calls** on each request.
* OMDb API data (like box office) is fetched only **once**, and can be stored during the application startup along with movie as the movies are from 1927-2010.
