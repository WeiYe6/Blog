package com.fengye.controller;

import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.AddTagDto;
import com.fengye.domain.dto.TagListDto;
import com.fengye.domain.dto.UpdateTagDto;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Resource
    private TagService tagService;

    //查询标签列表
    @GetMapping("/list")
    public ResponseResult getTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        //可以实现根据标签名 和 备注名进行标签的 模糊分页查询
        return tagService.getTagList(pageNum, pageSize, tagListDto);
    }

    //新增标签
    @PostMapping("")
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto){
        //新增标签功能
        if (addTagDto == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        return tagService.addTag(addTagDto);
    }

    //删除标签
    @DeleteMapping("/{id}")
    public ResponseResult deleteTagById(@PathVariable("id") Long id){
        if (id == null || id < 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        return tagService.deleteTagById(id);
    }

    //根据tagId获取tag信息
    @GetMapping("/{id}")
    public ResponseResult GetTagById(@PathVariable("id") Long id){
        if (id == null || id < 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        return tagService.GetTagById(id);
    }

    //更新标签
    @PutMapping("")
    public ResponseResult updateTag(@RequestBody UpdateTagDto updateTagDto){
        if (updateTagDto == null){
             throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        return tagService.updateTag(updateTagDto);
    }

    //查询全部标签
    @GetMapping("/listAllTag")
    public ResponseResult getListAllTag(){

        return tagService.listAllTag();
    }

}
