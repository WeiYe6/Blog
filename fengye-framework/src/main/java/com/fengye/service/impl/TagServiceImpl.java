package com.fengye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.AddTagDto;
import com.fengye.domain.dto.TagListDto;
import com.fengye.domain.dto.UpdateTagDto;
import com.fengye.domain.pojo.Tag;
import com.fengye.domain.vo.PageVO;
import com.fengye.domain.vo.TagVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.mapper.TagMapper;
import com.fengye.service.TagService;
import com.fengye.utils.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 曾伟业
 * @description 针对表【sg_tag(标签)】的数据库操作Service实现
 * @createDate 2024-02-02 15:39:48
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    @Override
    public ResponseResult getTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String tagName = tagListDto.getName();
        String tagRemark = tagListDto.getRemark();
        if (StringUtils.isNoneBlank(tagName)) {
            tagLambdaQueryWrapper.like(Tag::getName, tagName);
        }
        if (StringUtils.isNoneBlank(tagRemark)) {
            tagLambdaQueryWrapper.like(Tag::getRemark, tagRemark);
        }
        //分页查询
        Page<Tag> tagPage = new Page<>(pageNum, pageSize);
        page(tagPage, tagLambdaQueryWrapper);
        //以pageVO返回给前端
        PageVO pageVO = new PageVO();
        pageVO.setRows(tagPage.getRecords());
        pageVO.setTotal(tagPage.getTotal());
        return ResponseResult.okResult(pageVO);
    }

    @Override
    public ResponseResult addTag(AddTagDto addTagDto) {
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        boolean b = this.save(tag);
        if (!b) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTagById(Long id) {
        boolean b = this.removeById(id);
        if (!b) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult GetTagById(Long id) {
        Tag tag = this.getById(id);
        if (tag == null){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        TagVO tagVO = BeanCopyUtils.copyBean(tag, TagVO.class);

        return ResponseResult.okResult(tagVO);
    }

    @Override
    public ResponseResult updateTag(UpdateTagDto updateTagDto) {
        Tag tag = BeanCopyUtils.copyBean(updateTagDto, Tag.class);
        boolean b = this.updateById(tag);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        //获取全部标签列表
        List<Tag> tagList = this.list();
        //返回标签id 和 标签名 TagVO
        List<TagVO> tagVOS = BeanCopyUtils.copyBeanList(tagList, TagVO.class);
        return ResponseResult.okResult(tagVOS);
    }
}




