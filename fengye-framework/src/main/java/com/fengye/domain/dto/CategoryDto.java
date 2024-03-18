package com.fengye.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分类表Dto封装类：用于接收前端传过来的信息
 */
@Data
public class CategoryDto implements Serializable {
    /**
     * 分类名
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态
     */
    private String status;
}