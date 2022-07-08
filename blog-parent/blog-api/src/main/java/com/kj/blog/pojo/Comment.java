package com.kj.blog.pojo;

import lombok.Data;

@Data
public class Comment {

    private Long id;

    private String content;

    private Long createDate;

    private Long articleId;

    private Long authorId; // 评论者id

    private Long parentId; // 被评论id

    private Long toUid;    // 被评论者id

    private Integer level; // 评论层级
}
