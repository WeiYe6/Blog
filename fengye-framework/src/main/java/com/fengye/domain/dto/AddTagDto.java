package com.fengye.domain.dto;

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
public class AddTagDto {

    private String name;
    private String remark;

}