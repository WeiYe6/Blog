package com.fengye.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 友链dto类
 *
 * @author fengye
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateLinkStatusDto {

    private String id;
    private String status;

}