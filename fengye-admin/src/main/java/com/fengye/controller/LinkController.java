package com.fengye.controller;

import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.AddArticleDto;
import com.fengye.domain.dto.UpdateLinkStatusDto;
import com.fengye.domain.pojo.Link;
import com.fengye.domain.vo.PageVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.LinkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Resource
    private LinkService linkService;

    //分页查询友链列表
    @GetMapping("/list")
    public ResponseResult linkList(Integer pageNum, Integer pageSize, Link link){
        PageVO pageVO = linkService.linkList(pageNum, pageSize, link);
        return ResponseResult.okResult(pageVO);
    }

    //新增友链
    @PostMapping("")
    public ResponseResult addArticle(@RequestBody Link link){
        if (link == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        linkService.save(link);
        return ResponseResult.okResult();
    }

    //修改友链
    //1.根据id查询友链
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable("id") Long id){
        if (id == null || id <= 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }
    //2.修改友链状态
    @PutMapping("/changeLinkStatus")
    public ResponseResult updateLinkStatus(@RequestBody UpdateLinkStatusDto updateLinkStatusDto){
        if (updateLinkStatusDto == null) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        Link link = new Link();
        link.setId(Long.valueOf(updateLinkStatusDto.getId()));
        link.setStatus(updateLinkStatusDto.getStatus());
        boolean b = linkService.updateById(link);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }
    //3.修改友链信息
    @PutMapping("")
    public ResponseResult updateLinkStatus(@RequestBody Link link){
        if (link == null) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        boolean b = linkService.updateById(link);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }
    //删除友链
    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id") Long id){
        if (id == null || id < 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        boolean b = linkService.removeById(id);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }
}
