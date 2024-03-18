package com.fengye.runner;

import com.fengye.domain.pojo.Article;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.ArticleService;
import com.fengye.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fengye.constants.SystemConstants.Redis_ARTICLE_VIEWCOUNT;

/**
 * 启用预处理类
 * 对文章的浏览量进行预处理（缓存预热：启动项目时存储进redis中）
 */

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Resource
    private ArticleService articleService;
    @Resource
    private RedisCache redisCache;

    @Override
    public void run(String... args) {
        //启动项目时，以hash的类型把文章的浏览量存储进redis中
        //查询数据库中的所有文章信息
        List<Article> articleList = articleService.list();
        if (CollectionUtils.isEmpty(articleList)) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //封装map[] 我们只需要 文章的id、viewCount字段的信息即可
        Map<String, Integer> viewCountMap = articleList.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));

        //保存进redis中 key: "article:viewCount"  value: map集合[key(文章id): value(浏览量)]
        redisCache.setCacheMap(Redis_ARTICLE_VIEWCOUNT, viewCountMap);
    }
}
