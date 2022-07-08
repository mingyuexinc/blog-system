package com.kj.blog.controller;

import com.kj.blog.service.CommentsService;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.params.CommentParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){
        return commentsService.findCommentsByArticleId(id);
    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParams commentParams){
        return commentsService.comment(commentParams);
    }
}
