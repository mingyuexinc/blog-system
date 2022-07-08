package com.kj.blog.service;


import com.kj.blog.vo.Result;
import com.kj.blog.vo.params.CommentParams;

public interface CommentsService {
    /**
     * 根据文章id查询所有评论列表
     * @param id 
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/14 10:54
     */
    
    Result findCommentsByArticleId(Long id);

    /**
     * 保存评论信息
     * @param commentParams 
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/14 21:08
     */
    
    Result comment(CommentParams commentParams);
}
