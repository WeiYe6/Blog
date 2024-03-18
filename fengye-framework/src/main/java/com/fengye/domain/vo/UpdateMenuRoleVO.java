package com.fengye.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 角色菜单列表树vo封装类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) //让set方法 返回Article的类型
public class UpdateMenuRoleVO implements Serializable {
    /**
     * 表示菜单树
     */
    private List<MenuTreeVo> menus;

    /**
     *表示角色所关联的菜单权限id列表
     */
    private List<Long> checkedKeys;
}