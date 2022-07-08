package com.kj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kj.blog.mapper.CategoryMapper;
import com.kj.blog.pojo.Category;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.params.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId,Category::getCategoryName);  // 指定查询条件,节省查询时间
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        List<CategoryVo> categoryVoList = copyList(categories);
        return Result.success(categoryVoList);
    }

    @Override
    public Result findAllDetail() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        List<CategoryVo> categoryVoList = copyList(categories);
        return Result.success(categoryVoList);
    }

    @Override
    public Result categoryDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }

    private List<CategoryVo> copyList(List<Category> categories) {
        ArrayList<CategoryVo> categoryVoList  = new ArrayList<>();
        for (Category category : categories) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

}
