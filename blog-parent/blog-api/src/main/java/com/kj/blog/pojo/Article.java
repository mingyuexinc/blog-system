package com.kj.blog.pojo;

import lombok.Data;

@Data
public class Article {
    public static final int Article_Common = 0;  // 是否置顶,不置顶为0

    public static final int Article_TOP = 1;     // 是否置顶,置顶为1

    private Long id;  // id    // 文章

    private Integer commentCounts; // 评论数量

    private Long createDate;   // 创建时间

    private String summary;    // 简介

    private String title;      // 标题

    private Integer viewCounts;    // 浏览数量

    private Integer weight;  // 是否置顶

    private Long authorId;      // 作者ID

    private Long bodyId;        // 内容ID

    private Long categoryId;    // 类别ID
}
