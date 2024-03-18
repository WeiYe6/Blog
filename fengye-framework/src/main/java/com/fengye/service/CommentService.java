package com.fengye.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.Comment;

/**
* @author 曾伟业
* @description 针对表【sg_comment(评论表)】的数据库操作Service
* @createDate 2024-01-31 08:47:10
*/
public interface CommentService extends IService<Comment> {

    /**
     * 获取评论列表
     * @param commentType 用于区分是 文章评论区-"0" 还是 友链的评论区-"1"
     * @param articleId 文章id
     * @param pageNum 当前页码
     * @param pageSize 每页展示的条数
     * @return 评论列表
     */
    ResponseResult getCommentList(String commentType,Long articleId, Integer pageNum, Integer pageSize);

    /**
     * 发送评论
     * @param comment 新增的评论
     * @return 是否成功
     */
    ResponseResult saveComment(Comment comment);
}
