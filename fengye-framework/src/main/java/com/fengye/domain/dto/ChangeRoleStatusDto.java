package com.fengye.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改角色状态请求体
 * @author fegnye
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleStatusDto {

    private Long roleId;
    private String status;
}