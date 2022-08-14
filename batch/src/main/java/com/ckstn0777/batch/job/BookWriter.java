package com.ckstn0777.batch.job;

import com.ckstn0777.batch.dto.BookDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class BookWriter implements ItemWriter<BookDTO> {
    @Override
    public void write(List<? extends BookDTO> items) throws Exception {
        log.info("Writing books: {}", items);
    }
}
