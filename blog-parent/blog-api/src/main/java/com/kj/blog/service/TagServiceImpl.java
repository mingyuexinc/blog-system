package com.kj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kj.blog.mapper.TagMapper;
import com.kj.blog.pojo.Article;
import com.kj.blog.pojo.Tag;
import com.kj.blog.vo.ArticleVo;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService{

    @Autowired
    private TagMapper tagMapper;

    /**
     * pojo --> vo
     * @param tagList
     * @return java.util.List<com.kj.blog.vo.TagVo>
     * @create 2022/5/22 22:43
     */

    private List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    private TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }

    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public Result top(int limit) {
        List<Long> tagList = tagMapper.findHotsTagIds(limit);
        if (CollectionUtils.isEmpty(tagList)){
            return Result.success(Collections.emptyList());
        }
        // 最终需求的是tagIds和 tagName -->需要tag对象
        List<Tag> TagList = tagMapper.findTagsByTagIds(tagList);
        return Result.success(TagList);
    }

    @Override
    public Result findAll() {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(tagList));
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(tagList));
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }
}
