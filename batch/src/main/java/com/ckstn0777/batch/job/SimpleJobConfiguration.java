package com.ckstn0777.batch.job;

import com.ckstn0777.batch.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {
    private static final String PROPERTY_REST_API_URL = "rest.api.url"; // api 요청 url

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleJob(Step collectStep) { // 이런식으로 의존성 주입을 받을 수도 있구나
        return jobBuilderFactory.get("simpleJob")
                .start(collectStep)
                .build();
    }

    @Bean
    @JobScope
    public Step collectStep(ItemReader<BookDTO> reader, ItemWriter<BookDTO> writer) {
        return stepBuilderFactory.get("collectStep")
                .<BookDTO, BookDTO>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public ItemReader<BookDTO> reader(Environment environment, RestTemplate restTemplate) {
        // Rest API 로 데이터를 가져온다.
        return new RESTBookReader(environment.getRequiredProperty(PROPERTY_REST_API_URL),
                restTemplate);
    }

    @Bean
    public ItemWriter<BookDTO> writer() {
        return new BookWriter();
    }
}
