package com.fengye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengye.constants.SystemConstants;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.Comment;
import com.fengye.domain.vo.CommentVo;
import com.fengye.domain.vo.PageVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.mapper.CommentMapper;
import com.fengye.service.CommentService;
import com.fengye.service.UserService;
import com.fengye.utils.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 曾伟业
 * @description 针对表【sg_comment(评论表)】的数据库操作Service实现
 * @createDate 2024-01-31 08:47:10
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Resource
    private UserService userService;

    @Override //获取评论列表
    //分页获取该文章下的所有父评论 和 子评论
    public ResponseResult getCommentList(String commentType,Long articleId, Integer pageNum, Integer pageSize) {
        //分页获取该文章下的所有父评论（根评论）
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        commentLambdaQueryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT);
        //判断获取的是 文章的评论 还是友链的评论
        commentLambdaQueryWrapper.eq(Comment::getType,commentType);
        //分页查询
        Page<Comment> commentPage = new Page<>(pageNum, pageSize);
        page(commentPage, commentLambdaQueryWrapper);

        //对根评论进行排序（按时间的先后顺序排序）后评论的在前面展示（时间大：按照创建时间降序排序）
        List<Comment> commentList = commentPage.getRecords().stream()
                .sorted(Comparator.comparing(Comment::getCreateTime).reversed())
                .collect(Collectors.toList());
        //父评论封装返回
        List<CommentVo> commentVos = getCommentVos(commentList);

        //根据父评论的id，来查询对应的所有子评论
        for (CommentVo commentVo : commentVos) {
            //查询对应的子评论
            List<CommentVo> childrenCommentVos  = getChildrenComment(commentVo.getId());
            commentVo.setChildren(childrenCommentVos);
        }

        //分页返回 以pageVO返回给前端
        PageVO pageVO = new PageVO();
        pageVO.setRows(commentVos);
        pageVO.setTotal(commentPage.getTotal());

        return ResponseResult.okResult(pageVO);
    }

    //根据父评论的id，来查询对应的所有子评论，按照创建时间降序排序、 并赋值给VO
    //(注意子评论最多只能二级展开)
    private List<CommentVo> getChildrenComment(Long id) {
        //查询对应的子评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getRootId, id);
        commentLambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> childerCommentList = this.list(commentLambdaQueryWrapper);
        //子评论封装返回
        List<CommentVo> childrenCommentVos = getCommentVos(childerCommentList);
        return childrenCommentVos;
    }

    @Override
    public ResponseResult saveComment(Comment comment) {
        //评论不能为空
        if (StringUtils.isBlank(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        boolean b = this.save(comment);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    //-------------------------------下面是一些方便调用的方法-----------------------------------
    //把所以评论（包括子评论 和 父评论 以VO封装返回）
    @NotNull //非空
    private List<CommentVo> getCommentVos(List<Comment> commentList) {
        //copy所有评论， 以CommentVO的封装类返回
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(commentList, CommentVo.class);
        for (CommentVo commentVo : commentVos) {
            //要求1：根据发表父评论的userId（toCommentUserId），获取父评论人的nickName（toCommentUserName）
            //如果获取到的是父评论，那么toCommentUserId == -1，父评论 没有 父级（不需要返回）
            if (commentVo.getToCommentUserId() >= 0) {
                String rootUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(rootUserName);
            }
            //要求2：根据当前发表评论的userId（createBy），获取当前评论人的nickName（username）
            String userName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(userName);
        }
        return commentVos;
    }
}




