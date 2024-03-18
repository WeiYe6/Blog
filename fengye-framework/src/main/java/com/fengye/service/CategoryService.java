package com.fengye.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.CategoryDto;
import com.fengye.domain.pojo.Category;
import com.fengye.domain.vo.PageVO;

/**
* @author 曾伟业
* @description 针对表【sg_category(分类表)】的数据库操作Service
* @createDate 2024-01-28 17:38:02
*/
public interface CategoryService extends IService<Category> {

    /**
     * 展示分类列表
     * @return
     */
    ResponseResult getCategoryList();

    /**
     * 查询全部分类
     * @return 分类列表
     */
    ResponseResult listAllCategory();

    /**
     * 分页查询 分类数据列表
     * @param category 根据分类名 或者 状态进行查询
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @return
     */
    PageVO selectCategoryPage(Category category, Integer pageNum, Integer pageSize);

    /**
     * 新增分类
     * @param categoryDto 分类dto
     */
    void addCategory(CategoryDto categoryDto);

    /**
     * 修改分类
     * @param category 需要修改的分类
     */
    void updateCategory(Category category);
}
