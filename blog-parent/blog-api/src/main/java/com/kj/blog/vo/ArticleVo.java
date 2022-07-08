package com.kj.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.kj.blog.pojo.ArticleBody;
import com.kj.blog.pojo.Category;
import com.kj.blog.vo.params.CategoryVo;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ArticleVo {
    // 保证雪花算法生成的id精度
    // @JsonSerialize(using = ToStringSerializer.class)
    private String id;  // id    // ID

    private Integer commentCounts; // 评论数量

    private String summary;    // 简介

    private String title;      // 标题

    private Integer viewCounts;    // 浏览数量

    private Integer weight;        // 是否置顶

    private String createDate; // 创建时间

    private String author;     // 作者

    private ArticleBodyVo body; // 详情

    private List<TagVo> tags;  // 标签

    private CategoryVo category; // 类型
}
