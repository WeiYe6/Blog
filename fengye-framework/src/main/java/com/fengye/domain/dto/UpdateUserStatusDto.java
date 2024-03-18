package com.fengye.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户dto类
 *
 * @author fengye
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateUserStatusDto {

    private String userId;
    private String status;

}