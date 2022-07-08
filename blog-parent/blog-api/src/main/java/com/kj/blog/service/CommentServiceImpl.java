package com.kj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kj.blog.mapper.CommentMapper;
import com.kj.blog.pojo.Comment;
import com.kj.blog.pojo.SysUser;
import com.kj.blog.utils.UserThreadLocal;
import com.kj.blog.vo.CommentVo;
import com.kj.blog.vo.Result;
import com.kj.blog.vo.UserVo;
import com.kj.blog.vo.params.CommentParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentsService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result findCommentsByArticleId(Long id) {
        /* 1.根据文章id查询评论列表  （评论表）
        *  2.根据评论表中的作者id查出作者信息
        *  3.根据levelId查询，如果levelId =1，则要去查询是否有子评论
        *  4.如果有子评论，则根据parentId进行查询
        * */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id); // 查询articleid = id的记录
        queryWrapper.eq(Comment::getLevel,1);  //
        List<Comment> commentList = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(commentList);
        return Result.success(commentVoList);
    }

    @Override
    public Result comment(CommentParams commentParams) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParams.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParams.getContent());
        comment.setCreateDate(System.currentTimeMillis()); // 设置当前时间
        Long parent = commentParams.getParent();
        if (parent == null || parent == 0){
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null? 0: parent); // 没有父评论,设置0
        Long toUserId = commentParams.getToUserId();
        comment.setToUid(toUserId == null? 0: toUserId);
        commentMapper.insert(comment);
        return Result.success(null); // 注意这里并不需要返回数据给前端
    }

    private List<CommentVo> copyList(List<Comment> commentList) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(String.valueOf(comment.getId()));
        // 作者信息
        Long articleId = comment.getArticleId();
        UserVo userVo = sysUserService.findUserVoById(articleId);
        commentVo.setAuthor(userVo);
        // 子评论
        Integer level = comment.getLevel();
        if (level == 1){  // 才有子评论
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        if (level > 1){  // 才有父评论
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> commentList = commentMapper.selectList(queryWrapper);
        return copyList(commentList);
    }
}
