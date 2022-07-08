package com.kj.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.kj.blog.pojo.Comment;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo {

    // @JsonSerialize(using = ToStringSerializer.class) // 由分布式id太长,前端解析会丢失精度
    private String id;

    private UserVo author;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}
