package com.fengye.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热门文章 ArticleVO 返回给前端特定的字段
 * @author fengye
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVO {
    //热门文章id
    private Long id;
    //标题
    private String title;
    //访问量
    private Long viewCount;
    
}