package com.fengye.controller;


import com.fengye.domain.pojo.Article;
import com.fengye.domain.ResponseResult;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.ArticleService;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    //注入公共模块的ArticleService接口
    private ArticleService articleService;

    //----------------------------------测试mybatisPlus---------------------------------
    @GetMapping("/list")
    //Article是公共模块的实体类
    public List<Article> test(){
        //查询数据库的所有数据
        return articleService.list();
    }

    //----------------------------------测试统一响应格式-----------------------------------
    @GetMapping("/hotArticleList")
    //ResponseResult是fengye-framework工程的统一返回类
    public ResponseResult hotArticleList(){
        //查询热门文章，封装成ResponseResult返回
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    //----------------------------------分页查询文章的列表---------------------------------
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    //------------------------------------查询文章详情------------------------------------
   //路径参数形式的HTTP请求，注意下面那行只有加@PathVariable注解才能接收路径参数形式的HTTP请求
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {//注解里指定的id跟上一行保持一致
        if (id == null){
            throw new RuntimeException("参数错误");
        }
        //根据id查询文章详情
        return articleService.getArticleDetail(id);
    }

    //用户浏览文章时，更新该文章下的浏览量
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        if (id == null || id <= 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        return articleService.updateViewCount(id);
    }

}