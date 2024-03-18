package com.fengye.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengye.domain.pojo.ArticleTag;
import com.fengye.mapper.ArticleTagMapper;
import com.fengye.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
* @author 曾伟业
* @description 针对表【sg_article_tag(文章标签关联表)】的数据库操作Service实现
* @createDate 2024-02-05 22:42:58
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService {

}




