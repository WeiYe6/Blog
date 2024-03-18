package com.fengye.job;

import com.fengye.domain.pojo.Article;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.ArticleService;
import com.fengye.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fengye.constants.SystemConstants.Redis_ARTICLE_VIEWCOUNT;

/**
 * 定时任务
 */
@Component
public class myJob {

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleService articleService;

    //任务一: 同步redis中的浏览量 到数据库中
    // 把redis的数据同步到mysql数据库。避免redis宕机，丢失浏览量数据。
    //要求通过定时任务实现每隔10分钟把redis中的浏览量更新到mysql数据库中
    @Scheduled(cron = "0 0/10 * * * *") //定时任务表达式: 每隔10分钟触发
    public void updateViewCountJob(){ //执行什么逻辑的定时任务
        //从redis中获取文章的id以及浏览量，注意获取到的是双列集合
        Map<String, Integer> articleMap = redisCache.getCacheMap(Redis_ARTICLE_VIEWCOUNT);
        //把Map的value 转成单列集合
        List<Article> articles = articleMap.entrySet()
                .stream()
                //并把对应的文章id 和 浏览量赋值回去
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //批量更新数据库
        boolean b = articleService.updateBatchById(articles);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //测试一下：方便在控制台查看
        System.out.println("redis的文章浏览量数据已更新到数据库，现在的时间是: "+ LocalTime.now());
    }
}
