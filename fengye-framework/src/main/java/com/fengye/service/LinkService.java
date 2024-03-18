package com.fengye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.Link;
import com.fengye.domain.vo.PageVO;

/**
* @author 曾伟业
* @description 针对表【sg_link(友链)】的数据库操作Service
* @createDate 2024-01-29 10:47:36
*/
public interface LinkService extends IService<Link> {

    /**
     * 获取友链列表
     * @return 友链列表
     */
    ResponseResult getLinkList();

    /**
     * 分页查询友链列表
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param link 查询条件
     * @return 返回
     */
    PageVO linkList(Integer pageNum, Integer pageSize, Link link);
}
