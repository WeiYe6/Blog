package com.fengye.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 评论表
 * @TableName sg_comment
 */
@TableName(value ="sg_comment")
@Data
public class Comment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 评论类型（0代表文章评论，1代表友链评论）
     */
    private String type;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 根评论id
     */
    private Long rootId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 所回复的目标评论的userid
     */
    private Long toCommentUserId;

    /**
     * 回复目标评论id
     */
    private Long toCommentId;

    /**
     * 插入填充
     * 使用了mybatisPlus的字段自动填充
     * 比如：执行了插入/更新时 填充那些字段
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 插入填充
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 插入、更新填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    /**
     * 插入、更新填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}