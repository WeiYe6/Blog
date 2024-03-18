package com.fengye.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页VO封装类
 * @author fengye
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO {

    private List rows;
    private Long total;

}