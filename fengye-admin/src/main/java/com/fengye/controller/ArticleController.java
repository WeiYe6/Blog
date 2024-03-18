package com.fengye.controller;


import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.AddArticleDto;
import com.fengye.domain.dto.UpdateArticleDto;
import com.fengye.domain.pojo.Article;
import com.fengye.domain.vo.GetArticleByIdVO;
import com.fengye.domain.vo.PageVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Resource
    //注入公共模块的ArticleService接口
    private ArticleService articleService;

    //新增博文
    @PostMapping("")
    public ResponseResult addArticle(@RequestBody AddArticleDto addArticleDto){
        if (addArticleDto == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        return articleService.addArticle(addArticleDto);
    }

    //分页查询文章
    @GetMapping("list")
    public ResponseResult getArticlePageList(Integer pageNum, Integer pageSize, Article article){
        PageVO pageVO = articleService.getArticlePageList(pageNum, pageSize, article);
        return ResponseResult.okResult(pageVO);
    }

    //修改文章
    //根据id查询文章
    @GetMapping("{id}")
    public ResponseResult getArticleById(@PathVariable("id") Long id){
        if (id == null || id <= 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        GetArticleByIdVO getArticleByIdVO = articleService.getArticleById(id);
        return ResponseResult.okResult(getArticleByIdVO);
    }

    @PutMapping("")
    public ResponseResult updateArticle(@RequestBody UpdateArticleDto updateArticleDto){
        if (updateArticleDto == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        articleService.updateArticle(updateArticleDto);
        return ResponseResult.okResult();
    }

    //删除文章
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        if (id == null || id < 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        return articleService.deleteArticleById(id);
    }


}