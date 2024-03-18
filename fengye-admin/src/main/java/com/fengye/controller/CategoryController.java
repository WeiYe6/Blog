package com.fengye.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.CategoryDto;
import com.fengye.domain.dto.TagListDto;
import com.fengye.domain.pojo.Category;
import com.fengye.domain.vo.ExcelCategoryVO;
import com.fengye.domain.vo.PageVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.CategoryService;
import com.fengye.utils.BeanCopyUtils;
import com.fengye.utils.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Resource
    //注入公共模块的CategoryService接口
    private CategoryService categoryService;

    //查询全部分类
    @GetMapping("/listAllCategory")
    public ResponseResult getListAllCategory(){

        return categoryService.listAllCategory();
    }


    //分页查询分类列表
    @GetMapping("/list")
    public ResponseResult getPageCategoryList(Category category, Integer pageNum, Integer pageSize) {
        PageVO pageVo = categoryService.selectCategoryPage(category,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    //新增分类
    @PostMapping("")
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto) {
        if (categoryDto == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        categoryService.addCategory(categoryDto);
        return ResponseResult.okResult();
    }

    //修改分类
    //按照id进行查询
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable("id") Long id){
        if (id == null || id <= 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        Category category = categoryService.getById(id);
        return ResponseResult.okResult(category);
    }
    @PutMapping("")
    public ResponseResult updateCategory(@RequestBody Category category){
        if (category == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        categoryService.updateCategory(category);
        return ResponseResult.okResult(category);
    }

    //删除分类
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategoryById(@PathVariable("id") Long id){
        if (id == null || id <= 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        boolean b = categoryService.removeById(id);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    //---------------------------把分类数据写入到Excel并导出-----------------------------
    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('content:category:export')") //security的权限校验
    //注意返回值类型是void
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头，下载下来的Excel文件叫'分类.xlsx'
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> category = categoryService.list();

            List<ExcelCategoryVO> excelCategoryVos = BeanCopyUtils.copyBeanList(category, ExcelCategoryVO.class);
            //把数据写入到Excel中，也就是把ExcelCategoryVO实体类的字段作为Excel表格的列头
            //sheet方法里面的字符串是Excel表格左下角工作簿的名字
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVO.class).autoCloseStream(Boolean.FALSE).sheet("文章分类")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常,就返回失败的json数据给前端。AppHttpCodeEnum和ResponseResult是我们在fengye-framework工程写的类
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            //WebUtils是我们在fengye-framework工程写的类，里面的renderString方法是将json字符串写入到请求体，然后返回给前端
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
