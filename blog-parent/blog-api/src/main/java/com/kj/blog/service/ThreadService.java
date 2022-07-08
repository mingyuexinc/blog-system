package com.kj.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kj.blog.mapper.ArticleMapper;
import com.kj.blog.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ThreadService {

    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){

        Integer viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts + 1);  // 更新

        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        // 设置更新条件
        updateWrapper.eq(Article::getId,article.getId());
        updateWrapper.eq(Article::getViewCounts,viewCounts); // 乐观锁操作
        articleMapper.update(articleUpdate,updateWrapper);

    }
}
