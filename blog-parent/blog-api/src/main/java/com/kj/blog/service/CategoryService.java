package com.kj.blog.service;

import com.kj.blog.vo.Result;
import com.kj.blog.vo.params.CategoryVo;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);
    
    /**
     * 查找文章的所有类型
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/14 22:06
     */
    
    Result findAll();

    /**
     * 查找文章的所有类型的详细信息
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/16 16:57
     */
    
    Result findAllDetail();

    /**
     * 根据类别id查找文章
     * @param id 
     * @return com.kj.blog.vo.Result 
     * @create 2022/6/16 21:42
     */
    
    Result categoryDetailById(Long id);
}
