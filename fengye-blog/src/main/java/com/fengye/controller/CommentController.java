package com.fengye.controller;

import com.fengye.constants.SystemConstants;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.Comment;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 评论接口
 * @author fengye
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    //获取文章的评论
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        if (articleId <= 0){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return  commentService.getCommentList(SystemConstants.ARTICLE_COMMENT,articleId, pageNum, pageSize);
    }

    //发表评论
    @PostMapping("")
    public ResponseResult saveComment(@RequestBody Comment comment){
        if (comment == null) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        //评论内容不能为空
        if (StringUtils.isBlank(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        return commentService.saveComment(comment);
    }

    //获取友链的评论
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Long articleId,Integer pageNum,Integer pageSize){
        if (articleId <= 0){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return  commentService.getCommentList(SystemConstants.LINK_COMMENT,articleId, pageNum, pageSize);
    }

}
