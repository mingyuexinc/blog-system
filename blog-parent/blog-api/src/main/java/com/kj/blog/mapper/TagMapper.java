package com.kj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kj.blog.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TagMapper extends BaseMapper<Tag>{

    /**
     * 根据文章id查询列表
     * @param articleId 
     * @return java.util.List<com.kj.blog.pojo.Tag> 
     * @create 2022/5/27 11:14
     */
    
    List<Tag> findTagsByArticleId(Long articleId);

    /**
     * 查询最热的前n条标签
     * @param limit 
     * @return java.util.List<java.lang.Long> 
     * @create 2022/5/27 11:14
     */
    
    List<Long> findHotsTagIds(int limit);
    
    
    /**
     *
     * @param tagList 
     * @return java.util.List<com.kj.blog.pojo.Tag> 
     * @create 2022/5/27 11:24
     */
    
    List<Tag> findTagsByTagIds(List<Long> tagList);
}
