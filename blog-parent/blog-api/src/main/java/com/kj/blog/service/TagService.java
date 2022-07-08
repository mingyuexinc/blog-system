package com.kj.blog.service;

import com.kj.blog.vo.ArticleVo;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    /**
     * 根据文章id查询标签列表
     * @param articleId 
     * @return java.util.List<com.kj.blog.vo.TagVo> 
     * @create 2022/5/22 22:53
     */
    
    List<TagVo> findTagsByArticleId(Long articleId);

    /**
     * 返回limit上限的最热标签
     * @param limit 
     * @return com.kj.blog.vo.Result 
     * @create 2022/5/26 20:50
     */
    
    Result top(int limit);

    /**
     * 查找文章所有的标签
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/15 16:09
     */
    
    Result findAll();

    /**
     * 查看文章所有标签的详情
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/16 17:08
     */
    
    Result findAllDetail();

    /**
     * 根据文章类别id查询类别详情
     * @param id 
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/16 22:55
     */
    
    Result findDetailById(Long id);
}
