package com.kj.blog.controller;

import com.kj.blog.common.aop.LogAnnotation;
import com.kj.blog.common.cache.CacheAnnotation;
import com.kj.blog.mapper.ArticleMapper;
import com.kj.blog.service.ArticleService;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.params.ArticleParam;
import com.kj.blog.vo.params.PageParams;
import org.aspectj.asm.IModelFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// json数据格式交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /**
     * 首页 文章列表
     * @param pageParams 
     * @return com.kj.blog.vo.Result 
     * @create 2022/5/18 17:08
     */
    @PostMapping
    @LogAnnotation(module = "文章",operator = "获取文章列表")
    @CacheAnnotation(expire = 5*60*1000,name = "list_article")
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    /**
     * 首页  最热文章
     * @return com.kj.blog.vo.Result 
     * @create 2022/5/27 16:03
     */
    
    @PostMapping("hot")
    @CacheAnnotation(expire = 5*60*1000,name = "hot_article")
    public Result hotArticle(){
        int limit = 3;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页 最新文章
     * @return com.kj.blog.vo.Result
     * @create 2022/5/27 17:39
     */

    @PostMapping("new")
    @CacheAnnotation(expire = 5*60*1000,name = "news_article")
    public Result newArticle(){
        int limit = 3;
        return articleService.newArticle(limit);
    }

    /**
     * 首页 文章归档
     * @return com.kj.blog.vo.Result 
     * @create 2022/5/28 11:07
     */
    
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

}
