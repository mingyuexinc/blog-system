package com.kj.blog.dos;

import lombok.Data;

@Data
// 类似于vo的转换
public class Archives {
    private Integer year;

    private Integer month;

    private Long count;
}
