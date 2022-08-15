package com.ckstn0777.batch.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Table(name="book")
@Entity
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String title;

    private String author;

    @Builder
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }
}
