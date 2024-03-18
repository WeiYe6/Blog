package com.fengye.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 评论VO 封装类
 * @author fengye
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {

    //评论id
    private Long id;
    //文章id（评论的文章id）
    private Long articleId;
    //根评论id（父评论id）
    private Long rootId;
    //评论内容
    private String content;
    //发根评论的userId（发表父评论的userId）
    private Long toCommentUserId;
    //发根评论的userName（发表父评论的userName）
    private String toCommentUserName;
    //回复目标评论id（被回复的评论id：因为该评论最多只能二级展开，所以在父评论下的所有子评论 均平级）
    private Long toCommentId;
    //当前评论的创建人id
    private Long createBy;
    //创建时间
    private Date createTime;
    //评论是谁发的（发表该条评论人的userName）
    private String username;
    //查询子评论
    private List<CommentVo> children;

}