package com.fengye.controller;

import com.fengye.domain.ResponseResult;
import com.fengye.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/category")
public class CategoryController  {

    @Resource
    //注入公共模块的CategoryService接口
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    //ResponseResult是fengye-framework工程的统一返回类
    public ResponseResult getCategoryList(){

        return categoryService.getCategoryList();
    }
}
