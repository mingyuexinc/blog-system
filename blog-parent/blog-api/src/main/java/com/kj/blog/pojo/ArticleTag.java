package com.kj.blog.pojo;

import lombok.Data;

@Data
public class ArticleTag {
    private Long id;

    private Long articleId;

    private Long tagId;
}
