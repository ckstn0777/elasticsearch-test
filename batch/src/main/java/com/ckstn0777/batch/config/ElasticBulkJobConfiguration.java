package com.ckstn0777.batch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.ckstn0777.batch.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
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
    @JobScope
    public Step elasticBulkStep(JpaPagingItemReader<Book> jpaBookReader, ItemWriter<Book> elasticBulkWriter) {
        return stepBuilderFactory.get("elasticBulkStep")
                .<Book, Book>chunk(1000)
                .reader(jpaBookReader)
                .writer(elasticBulkWriter)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Book> jpaBookReader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(1000)
                .queryString("SELECT b FROM Book b")
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<Book> elasticBulkWriter(ElasticsearchClient elasticsearchClient) {
        return books -> {
            log.info("Writing books: {}", books);
            BulkRequest.Builder br = new BulkRequest.Builder();

            for (Book book : books) {
                br.operations(op -> op
                        .index(idx -> idx
                                .index("public_book")
                                .id("public_book_" + book.getId().toString())
                                .document(book)
                        )
                );
            }

            BulkResponse result = elasticsearchClient.bulk(br.build());
            if (result.errors()) {
                log.error("Bulk had errors");
                for (BulkResponseItem item: result.items()) {
                    if (item.error() != null) {
                        log.error(item.error().reason());
                    }
                }
            }
        };
    }
}
