package com.ckstn0777.batch.config;

import com.ckstn0777.batch.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ElasticBulkJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job elasticBulkJob(Step elasticBulkStep) {
        return jobBuilderFactory.get("elasticBulkJob")
                .start(elasticBulkStep)
                .build();
    }

    @Bean
    public Step elasticBulkStep() {
        return stepBuilderFactory.get("elasticBulkStep")
                .<Book, Book>chunk(10)
                .reader(jpaBookReader())
                .writer(elasticBulkWriter())
                .build();
    }

    @Bean
    public ItemReader<Book> jpaBookReader() {
        // TODO : 구현 예정
        return null;
    }

    @Bean
    public ItemWriter<Book> elasticBulkWriter() {
        // TODO : 구현 예정
        return null;
    }
}
