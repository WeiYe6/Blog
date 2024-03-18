package com.fengye.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签dto类
 * @author fengye
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TagVO {

    private Long id;
    private String name;
    private String remark;

}