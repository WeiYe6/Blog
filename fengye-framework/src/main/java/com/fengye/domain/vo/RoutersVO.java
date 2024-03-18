package com.fengye.domain.vo;


import com.fengye.domain.pojo.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVO {

    private List<Menu> menus;
}
