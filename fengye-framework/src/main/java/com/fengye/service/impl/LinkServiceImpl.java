package com.fengye.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengye.constants.SystemConstants;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.Link;
import com.fengye.domain.vo.LinkVO;
import com.fengye.domain.vo.PageVO;
import com.fengye.mapper.LinkMapper;
import com.fengye.service.LinkService;
import com.fengye.utils.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
* @author 曾伟业
* @description 针对表【sg_link(友链)】的数据库操作Service实现
* @createDate 2024-01-29 10:47:36
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService {


    @Override
    public ResponseResult getLinkList() {
        //获取友链列表
        //要求： 获取到的数据必须是通过审核的
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> linkList = this.list(linkLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(linkList)){
            throw new RuntimeException("数据不存在异常");
        }
        //封装成LinkVO返回给前端
        List<LinkVO> linkVOS = BeanCopyUtils.copyBeanList(linkList, LinkVO.class);

        return ResponseResult.okResult(linkVOS);
    }

    @Override
    public PageVO linkList(Integer pageNum, Integer pageSize, Link link) {
        //分页查询友链列表
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String name = link.getName();
        //根据友链名称模糊查询
        if (StringUtils.isNoneBlank(name)) {
            linkLambdaQueryWrapper.like(Link::getName, name);
        }
        //根据状态查询
        String status = link.getStatus();
        if (StringUtils.isNoneBlank(status)) {
            linkLambdaQueryWrapper.eq(Link::getStatus, status);
        }
        //分页查询
        Page<Link> linkPage = new Page<>(pageNum, pageSize);
        page(linkPage, linkLambdaQueryWrapper);

        //PageVO返回
        PageVO pageVO = new PageVO();
        pageVO.setRows(linkPage.getRecords());
        pageVO.setTotal(linkPage.getTotal());
        return pageVO;
    }
}




