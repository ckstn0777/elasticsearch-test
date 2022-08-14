package com.ckstn0777.batch.job;

import com.ckstn0777.batch.dto.BookDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class RESTBookReader implements ItemReader {
    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextBookIndex;
    private List<BookDTO> bookData;

    RESTBookReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("Reading the information of the next book");

        if (bookDataIsNotInitialized()) {
            bookData = fetchBookDataFromAPI();
        }

        return null;
    }

    private boolean bookDataIsNotInitialized() {
        return this.bookData == null;
    }

    private List<BookDTO> fetchBookDataFromAPI() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("numOfRows", "100")
                .queryParam("pageNo", 1);

        log.info("Fetching book data from an external API by using the url: {}", uriBuilder.toUriString());

        ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        // Json parsing 하는 부분... 근데 이게 최선인가 싶네
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseObject = objectMapper.readValue(response.getBody(),
                new TypeReference<Map<String, Object>>() {});

        Map<String, Object> responseProperty = (Map<String, Object>) responseObject.get("response");
        Map<String, Object> bodyProperty = (Map<String, Object>) responseProperty.get("body");
        Map<String, Object> itemsProperty = (Map<String, Object>) bodyProperty.get("items");

        BookDTO[] bookData = objectMapper.readValue(objectMapper.writeValueAsString(itemsProperty.get("item")), BookDTO[].class);

        int numOfRows = Integer.parseInt(bodyProperty.get("numOfRows").toString());
        int pageNo = Integer.parseInt(bodyProperty.get("pageNo").toString());
        int totalCount = Integer.parseInt(bodyProperty.get("totalCount").toString());

        return Arrays.asList(bookData);
    }

}
