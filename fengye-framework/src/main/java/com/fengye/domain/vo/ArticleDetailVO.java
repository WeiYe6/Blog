package com.fengye.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 封装文章详情的实体类VO，只把需要的字段返回给前端
 * @author fegnye
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVO {

    private Long id;
    //标题
    private String title;
    //文章摘要(多余的可删)
    private String summary;
    //文章的分类id
    private Long categoryId;
    //文章的内容
    private String content;
    //所属分类名
    private String categoryName;
    //缩略图(多余的可删)
    private String thumbnail;
    // 是否允许评论 1是，0否
    private String isComment;
    //访问量
    private Long viewCount;
    //创建时间
    private Date createTime;

}