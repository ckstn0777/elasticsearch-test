package com.ckstn0777.batch.reader;

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

    private int currentPage;   // 현재 페이지
    private int totalPage;     // 전체 페이지
    private int numOfRows;     // 페이지 당 데이터 수
    private int nextBookIndex; // 가져올 데이터 인덱스

    private List<BookDTO> bookData;

    public RESTBookReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;

        currentPage = 1;
        numOfRows = 1000; // 한 페이지당 1000건씩 요청
        nextBookIndex = 0;
    }

    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("Reading the information of the next book");

        if (bookDataIsNotInitialized()) { // 초기 데이터가 없다면 호출
            bookData = fetchBookDataFromAPI(currentPage, numOfRows);
        }

        BookDTO nextBook = null;

        if (nextBookIndex < bookData.size()) {
            nextBook = bookData.get(nextBookIndex);
            nextBookIndex += 1;

            // 다음 내용이 없다면 currentPage를 증가시키는데.. totalPage보다 넘는 경우는 종료시킴
            if (nextBookIndex == bookData.size()) {
                if (totalPage <= currentPage * numOfRows) return null;

                currentPage += 1;
                nextBookIndex = 0;
                bookData = null;
            }
        }

        log.info("Found book: {}", nextBook);

        return nextBook;
    }

    private boolean bookDataIsNotInitialized() {
        return this.bookData == null;
    }

    private List<BookDTO> fetchBookDataFromAPI(int currentPage, int numOfRows) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", currentPage);

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

//        int numOfRows = Integer.parseInt(bodyProperty.get("numOfRows").toString());
//        int pageNo = Integer.parseInt(bodyProperty.get("pageNo").toString());
        totalPage = Integer.parseInt(bodyProperty.get("totalCount").toString());

        return Arrays.asList(bookData);
    }

}
