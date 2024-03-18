package com.fengye.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengye.constants.SystemConstants;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.CategoryDto;
import com.fengye.domain.pojo.Article;
import com.fengye.domain.pojo.Category;
import com.fengye.domain.vo.CategoryVO;
import com.fengye.domain.vo.PageVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.mapper.CategoryMapper;
import com.fengye.service.ArticleService;
import com.fengye.service.CategoryService;
import com.fengye.utils.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author 曾伟业
* @description 针对表【sg_category(分类表)】的数据库操作Service实现
* @createDate 2024-01-28 17:38:02
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

    @Resource
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //构造条件: 只展示发布了正式文章的(非草稿文章) 分类列表
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //查询文章列表中的 分类id，且把分类id收集起来（注意去重 Set）
        List<Article> articleList = articleService.list(articleWrapper);
        Set<Long> categoryId = articleList.stream().map(Article::getCategoryId).collect(Collectors.toSet());

        //此时获取到的分类id就是发布了文章的，根据该id去查询分类表
        //构造条件: 分类id为上面的，且状态为正常的-0
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.in(Category::getId, categoryId);
        categoryLambdaQueryWrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        //查询到的是能正常展示的分类数据
        List<Category> categoryList = this.list(categoryLambdaQueryWrapper);

        //使用CategoryVO返回给前端
        List<CategoryVO> categoryVOS = BeanCopyUtils.copyBeanList(categoryList, CategoryVO.class);

        return ResponseResult.okResult(categoryVOS);
    }

    @Override
    public ResponseResult listAllCategory() {
        //查询全部分类列表
        List<Category> categoryList = this.list();

        //只返回分类id 和 分类名称即可：CategoryVO
        List<CategoryVO> categoryVOS = BeanCopyUtils.copyBeanList(categoryList, CategoryVO.class);
        return ResponseResult.okResult(categoryVOS);
    }

    @Override
    public PageVO selectCategoryPage(Category category, Integer pageNum, Integer pageSize) {
        //分页查询 分类信息
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据分类名进行查询
        String categoryName = category.getName();
        if (StringUtils.isNoneBlank(categoryName)){
            categoryLambdaQueryWrapper.like(Category::getName, categoryName);
        }
        //根据分类状态进行查询
        String status = category.getStatus();
        if (StringUtils.isNoneBlank(status)){
            categoryLambdaQueryWrapper.eq(Category::getStatus, status);
        }
        Page<Category> categoryPage = new Page<>(pageNum, pageSize);
        page(categoryPage, categoryLambdaQueryWrapper);

        //封装返回
        PageVO pageVO = new PageVO();
        pageVO.setRows(categoryPage.getRecords());
        pageVO.setTotal(categoryPage.getTotal());

        return pageVO;
    }

    @Override
    public void addCategory(CategoryDto categoryDto) {
        //新增分类
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        boolean b = this.save(category);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public void updateCategory(Category category) {
        //修改分类
        boolean b = this.updateById(category);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }
}




