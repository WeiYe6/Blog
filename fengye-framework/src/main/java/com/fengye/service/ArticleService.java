package com.fengye.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fengye.domain.dto.AddArticleDto;
import com.fengye.domain.dto.UpdateArticleDto;
import com.fengye.domain.pojo.Article;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.vo.GetArticleByIdVO;
import com.fengye.domain.vo.PageVO;

/**
* @author 曾伟业
* @description 针对表【sg_article(文章表)】的数据库操作Service
* @createDate 2024-01-28 08:54:50
*/
public interface ArticleService extends IService<Article> {
    /**
     * 热门文章列表展示
     * @return 热门文章列表
     */
    ResponseResult hotArticleList();

    /**
     * 分页查询文章的列表
     * @param pageNum 当前页数
     * @param pageSize 每页显示条数
     * @param categoryId 分类id
     * @return 文章的列表
     */
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    /**
     * 根据id 获取文章详情
     * @param id 文章id
     * @return 文章详情
     */
    ResponseResult getArticleDetail(Long id);

    /**
     * 用户浏览文章时，更新该文章下的浏览量
     * @param id 该文章的id
     * @return 是否成功
     */
    ResponseResult updateViewCount(Long id);

    /**
     * 新增博文
     * @param addArticleDto 新增的博文内容
     * @return 返回结果
     */
    ResponseResult addArticle(AddArticleDto addArticleDto);

    /**
     * 分页查询文章
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param article 需要查询的字段
     * @return 返回结果
     */
    PageVO getArticlePageList(Integer pageNum, Integer pageSize, Article article);

    /**
     * 根据id查询文章
     * @param id 文章id
     * @return
     */
    GetArticleByIdVO getArticleById(Long id);

    /**
     * 修改文章
     * @param updateArticleDto 修改的文章信息
     */
    void updateArticle(UpdateArticleDto updateArticleDto);

    /**
     * 删除文章
     * @param id 要删除文章的id
     * @return
     */
    ResponseResult deleteArticleById(Long id);
}
