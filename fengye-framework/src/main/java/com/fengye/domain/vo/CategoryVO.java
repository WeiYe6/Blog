package com.fengye.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类表的VO封装类
 * @author fengye
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO {

    //分类id
    private Long id;
    //分类标题
    private String name;
}
