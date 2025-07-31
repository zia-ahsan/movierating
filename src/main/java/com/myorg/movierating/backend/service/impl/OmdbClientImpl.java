package com.myorg.movierating.backend.service.impl;

import com.myorg.movierating.backend.exception.OmdbApiException;
import com.myorg.movierating.backend.service.OmdbClient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Pattern;

@Profile("!test")
@Service
@Slf4j
public class OmdbClientImpl implements OmdbClient {

    private static final Pattern BOX_OFFICE_PATTERN = Pattern.compile("\\$(\\d+(,\\d{3})*)");
    private final RestTemplate restTemplate;
    @Value("${omdb.api.key}")
    private String apiKey;

    public OmdbClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Long fetchBoxOffice(final String title, final Integer year) {
        try {
            String url = String.format("https://www.omdbapi.com/?t=%s&y=%d&apikey=%s", title.replace(" ", "+"), year, apiKey);
            var response = restTemplate.getForObject(url, OmdbResponse.class);
            if (response != null && response.boxOffice != null) {
                return parseBoxOffice(response.boxOffice);
            }
        } catch (Exception e) {
            log.error("Failed to fetch data from OMDB", e);
            throw new OmdbApiException("Failed to fetch data from OMDB for title: " + title, e);
        }
        return 0L;
    }

    private Long parseBoxOffice(String boxOffice) {
        if (boxOffice == null || boxOffice.isEmpty() || boxOffice.equalsIgnoreCase("N/A")) return 0L;

        try {
            return Long.parseLong(
                    boxOffice.replaceAll("[$,]", "")
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OmdbResponse {

        @JsonProperty("BoxOffice")
        private String boxOffice;

        public String getBoxOffice() {
            return boxOffice;
        }

        public void setBoxOffice(String boxOffice) {
            this.boxOffice = boxOffice;
        }
    }

}
