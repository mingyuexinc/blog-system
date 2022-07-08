package com.kj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kj.blog.dos.Archives;
import com.kj.blog.mapper.*;
import com.kj.blog.pojo.Article;
import com.kj.blog.pojo.ArticleBody;
import com.kj.blog.pojo.ArticleTag;
import com.kj.blog.pojo.SysUser;
import com.kj.blog.utils.UserThreadLocal;
import com.kj.blog.vo.ArticleBodyVo;
import com.kj.blog.vo.ArticleVo;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.TagVo;
import com.kj.blog.vo.params.ArticleParam;
import com.kj.blog.vo.params.CategoryVo;
import com.kj.blog.vo.params.PageParams;
import org.apache.ibatis.annotations.Results;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticle(PageParams pageParams){
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /*
//        * 1.分页、自定义条件查询数据库表
//        * */
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId()!=null){
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if(pageParams.getTagId()!=null){
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size()>0){
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//        // 先按照是否置顶排序,然后时间倒序排列
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> pageRecords = articlePage.getRecords();
//        /*
//         * 2.转换为voList类型(注意不能直接返回)
//         * */
//        // 要查询tag和service
//        List<ArticleVo> articleVoList = copyList(pageRecords,true,true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 按照浏览量降序排序
        queryWrapper.orderByDesc(Article::getViewCounts);
        // 只操作这两条数据
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);  // limit后面要有空格
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        // vo转换  查询条件设置
        return Result.success(copyList(articleList,false,false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 按照文章创建时间降序排序
        queryWrapper.orderByDesc(Article::getCreateDate);
        // 只操作这两条数据
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);  // limit后面要有空格
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        // vo转换  查询条件设置
        return Result.success(copyList(articleList,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList =  articleMapper.listArchives();
        return Result.success(archivesList);
    }

    /*
    * 1.根据id查询文章信息
    * 2.根据bodyId和categoryId做关联查询
    * */
    @Override
    public Result findArticleById(Long articleId) {
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article,true,true,true,true);
        // 使用线程池技术,让更新操作在线程池中进行
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }


    @Override
    public Result publish(ArticleParam articleParam) {
        Article article = new Article();
        SysUser sysUser = UserThreadLocal.get();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        articleMapper.insert(article);
        List<TagVo> tags = articleParam.getTags();
        if (tags!=null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article); // 更新后body才会保留

        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        return Result.success(articleVo);
    }

    private List<ArticleVo> copyList(List<Article> pageRecords,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : pageRecords) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }

    // 重载方法,避免修改原有实现
    private List<ArticleVo> copyList(List<Article> pageRecords,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : pageRecords) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    /**
     * 将实体类转换为vo类型
     * @param article
     * @return com.kj.blog.vo.ArticleVo
     * @create 2022/5/21 20:26
     */

    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);
        // 属性转换 注意vo层和pojo中部分属性类型不同,单独处理
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 不是所有的接口都需要标签、作者信息
        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname()); //
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
