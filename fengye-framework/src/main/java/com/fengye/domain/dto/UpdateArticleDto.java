package com.fengye.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 文章表 新增博文Dto
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleDto implements Serializable {
    /**
     *博文id
     */

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 所属分类id
     */
    private Long categoryId;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 是否置顶（0否，1是）
     */
    private String isTop;

    /**
     * 状态（0已发布，1草稿）
     */
    private String status;


    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;

    /**
     * tags属性是一个List集合，用于接收文章关联标签的id
     */
    private List<Long> tags;
}