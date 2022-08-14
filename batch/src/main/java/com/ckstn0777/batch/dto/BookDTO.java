package com.ckstn0777.batch.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 먼저 DTO를 간단하게 만든다. RestAPI 로 읽어온 데이터를 담고, 쓰기 위한 용도이다.
 */
@Getter @Setter
public class BookDTO {
    private String id;
    private String name;
}
