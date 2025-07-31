# how_to_test.md

This guide explains how to test the **MovieRating** API endpoints securely using **Postman**, with automated OAuth2 token handling.

## Prerequisites

* Postman installed (or Postman Web)
* The following two files (provided in the assignment submission bundle):

    * `MovieRating_Postman_Environment.json`
    * `MovieRating_Postman_Collection_WithAuth.json`
* Local backend instance running (e.g., `https://localhost:8443`)

## Steps to Test

1. **Open Postman**

2. **Import Both Files**

    * Go to **File > Import**
    * Select:

        * `MovieRating_Postman_Environment.json`
        * `MovieRating_Postman_Collection_WithAuth.json`

3. **Activate Environment**

    * From the top-right dropdown, select:
      `MovieRatingEnv`

4. **Trigger a Request**

    * Expand the collection: `MovieRating API`
    * Choose any endpoint (e.g., `Get: Is Best Picture Winner`)
    * Click **"Send"** with a movie title in the url

## Behind the Scenes (Automated)

* A **Pre-request Script** in the collection:

    * Automatically fetches a new **access token** from **Microsoft Entra External ID** using:

        * `client_id`, `client_secret`, `scope`, `token_url`
    * Stores it in the environment as `access_token`

* The token is then attached **automatically** to every secured request as:

  ```
  Authorization: Bearer {{access_token}}
  ```

* If the token is expired, it will **auto-refresh** before each request.