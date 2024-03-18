package com.fengye.controller;

import com.fengye.domain.ResponseResult;
import com.fengye.service.CategoryService;
import com.fengye.service.LinkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/link")
public class LinkController {

    @Resource
    //注入公共模块的LinkService接口
    private LinkService linkService;

    @GetMapping("/getAllLink")
    //ResponseResult是fengye-framework工程的统一返回类
    public ResponseResult getLinkList(){
        //获取友链列表
        return linkService.getLinkList();
    }
}
