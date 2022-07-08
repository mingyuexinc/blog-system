package com.kj.blog.service;

import com.kj.blog.vo.Result;
import com.kj.blog.vo.params.ArticleParam;
import com.kj.blog.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return com.kj.blog.vo.Result 
     * @create 2022/5/18 17:17
     */
    
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return com.kj.blog.vo.Result
     * @create 2022/5/27 17:39
     */

    Result hotArticle(int limit);


    /**
     * 最新文章
     * @param limit
     * @return com.kj.blog.vo.Result
     * @create 2022/5/27 17:52
     */

    Result newArticle(int limit);
    
    
    /**
     * 文章归档
     * @return com.kj.blog.vo.Result 
     * @create 2022/5/28 21:32
     */
    
    Result listArchives();

    /**
     * 查看文章详情
     * @param articleId
     * @return com.kj.blog.vo.Result
     * @create 2022/6/12 11:53
     */
    Result findArticleById(Long articleId);

    /**
     * 文章发布服务
     * @param articleParam 
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/15 16:26
     */
    
    Result publish(ArticleParam articleParam);
}
