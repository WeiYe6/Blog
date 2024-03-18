package com.fengye.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.AddTagDto;
import com.fengye.domain.dto.TagListDto;
import com.fengye.domain.dto.UpdateTagDto;
import com.fengye.domain.pojo.Tag;

/**
* @author 曾伟业
* @description 针对表【sg_tag(标签)】的数据库操作Service
* @createDate 2024-02-02 15:39:48
*/
public interface TagService extends IService<Tag> {

    /**
     * 可以实现根据标签名 和 备注名进行标签的 模糊分页查询
     * @param pageNum  当前页面
     * @param pageSize 每页显示的条数
     * @param tagListDto 标签dto 标签名、备注名
     * @return
     */
    ResponseResult getTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    /**
     * 新增标签
     * @param addTagDto 新增的内容
     * @return 返回成功
     */
    ResponseResult addTag(AddTagDto addTagDto);

    /**
     * 删除标签
     * @param id 删除标签的id
     * @return 是否删除
     */
    ResponseResult deleteTagById(Long id);

    /**
     * 根据id获取标签信息
     * @param id 标签id
     * @return 结果
     */
    ResponseResult GetTagById(Long id);

    /**
     * 更新tag标签
     * @param updateTagDto 更新的内容
     * @return 返回更新结果
     */
    ResponseResult updateTag(UpdateTagDto updateTagDto);

    /**
     * 获取全部标签列表
     * @return 标签列表
     */
    ResponseResult listAllTag();

}
