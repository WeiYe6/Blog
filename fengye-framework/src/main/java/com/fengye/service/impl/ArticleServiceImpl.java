package com.fengye.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengye.constants.SystemConstants;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.AddArticleDto;
import com.fengye.domain.dto.UpdateArticleDto;
import com.fengye.domain.pojo.Article;
import com.fengye.domain.pojo.ArticleTag;
import com.fengye.domain.pojo.Category;
import com.fengye.domain.vo.*;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.mapper.ArticleMapper;
import com.fengye.service.ArticleService;
import com.fengye.service.ArticleTagService;
import com.fengye.service.CategoryService;
import com.fengye.utils.BeanCopyUtils;
import com.fengye.utils.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.fengye.constants.SystemConstants.Redis_ARTICLE_VIEWCOUNT;

/**
 * @author 曾伟业
 * @description 针对表【sg_article(文章表)】的数据库操作Service实现
 * @createDate 2024-01-28 08:54:50
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleTagService articleTagService;

    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult hotArticleList() {

        //查询热门文章，封装成ResponseResult返回。把所有查询条件写在queryWrapper里面
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //查询的不能是草稿。也就是Status字段只能是0
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序。也就是根据ViewCount字段降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只能查询出来10条消息。当前显示第一页的数据，每页显示10条数据
        Page<Article> page = new Page<>(SystemConstants.ARTICLE_STATUS_CURRENT, SystemConstants.ARTICLE_STATUS_SIZE);
        page(page, queryWrapper);

        //获取最终的 查询结果，把结果封装在 Article实体类 里面会有很多不需要的字段
        List<Article> articles = page.getRecords();

        //解决: 把结果封装在HotArticleVo实体类里面，在HotArticleVo实体类只写我们要的字段
//        List<HotArticleVO> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVO articleVO = new HotArticleVO();
//            //使用spring提供的BeanUtils类，来实现bean拷贝。第一个参数是源数据，第二个参数是目标数据，把源数据拷贝给目标数据
//            //虽然article里面有很多不同的字段，但是articleVO里面只有3个字段(没有具体数据)，所以拷贝之后，就能把articleVO里面的三个字段填充具体数据
//            BeanUtils.copyProperties(article,articleVO); //article就是Article实体类，articleVO就是HotArticleVo实体类
//            //把我们要的数据(也就是上一行的articleVO)添加进集合中
//            articleVos.add(articleVO);
//        }
        for (Article article : articles) {
            Long articleId = article.getId();
            //文章的浏览量从redis中获取
            Long viewCountVO = getViewCountVO(articleId);
            article.setViewCount(viewCountVO);
        }

        //集合拷贝 以VO返回
        List<HotArticleVO> hotArticleVOS = BeanCopyUtils.copyBeanList(articles, HotArticleVO.class);

        return ResponseResult.okResult(hotArticleVOS);
    }


    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //首页: 查询所有文章
        //分类页面: 查询对应分类下的文章
        //要求：只能查询发布了的文章， 且置顶的文章要实现在最前面
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //categoryId可空，如果为空时，则不作为查询条件，否则需要作为查询条件
        if (categoryId != null && categoryId > 0) {
            articleLambdaQueryWrapper.eq(Article::getCategoryId, categoryId);
        }
        //只能查询发布了的文章
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //且置顶的文章要实现在最前面
        articleLambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        page(articlePage, articleLambdaQueryWrapper);

        /*
          解决categoryName字段没有返回值的问题
          用categoryId来查询categoryName(category表的name字段)，也就是查询'分类名称'。有两种方式来实现，如下
         */
        List<Article> articles = articlePage.getRecords();
        //方式一: for循环
        for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            //文章的浏览量从redis中获取
            Long viewCountVO = getViewCountVO(article.getId());
            article.setViewCount(viewCountVO);
            article.setCategoryName(category.getName());
        }

        //方式二: Stream流
//        articles.stream().map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
//                .collect(Collectors.toList());


        //把文章列表封装成 ArticleListVO返回
        List<ArticleListVO> articleListVos = BeanCopyUtils.copyBeanList(articlePage.getRecords(), ArticleListVO.class);

        //以PageVO的结果返回，添加总数
        PageVO pageVo = new PageVO();
        pageVo.setRows(articleListVos);
        pageVo.setTotal(articlePage.getTotal());

        return ResponseResult.okResult(pageVo);
    }


    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = this.getById(id);
        if (article == null){
            throw new RuntimeException("数据不存在异常"); //如果没有该id下的文章，则返回异常;
        }

        //根据分类id获取分类名
        article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());

        //返回文章VO封装类
        ArticleDetailVO articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVO.class);

        //文章的浏览量从redis中获取, 并赋值给VO
        Long viewCountVO = getViewCountVO(id);
        articleDetailVo.setViewCount(viewCountVO);

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis 根据文章的id的，更新该浏览量（+1）
        //参数：hash的key，hkey里面的属性，递增的值
        redisCache.incrementCacheMapValue(Redis_ARTICLE_VIEWCOUNT, id.toString(), 1);
        return ResponseResult.okResult();
    }


    @Override
    @Transactional
    public ResponseResult addArticle(AddArticleDto addArticleDto) {
        //新增博文
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        boolean b = this.save(article);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //新增博文对应的tag标签
        //获取到新增博文的id 以及该博文对应的标签列表-- 一并存储进 文章-标签（中间表）
        List<ArticleTag> articleTagList = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        boolean b1 = articleTagService.saveBatch(articleTagList);
        if (!b1){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        return ResponseResult.okResult();
    }

    //后台：分页展示文章列表
    @Override
    public PageVO getArticlePageList(Integer pageNum, Integer pageSize, Article article) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String title = article.getTitle();
        if(StringUtils.isNoneBlank(title)){
            articleLambdaQueryWrapper.like(Article::getTitle, title);
        }
        String summary = article.getSummary();
        if (StringUtils.isNoneBlank(summary)) {
            articleLambdaQueryWrapper.like(Article::getSummary, summary);
        }

        //分页查询
        Page<Article> articlePage = new Page<>();
        page(articlePage, articleLambdaQueryWrapper);
        PageVO pageVO = new PageVO();
        pageVO.setRows(articlePage.getRecords());
        pageVO.setTotal(articlePage.getTotal());
        return pageVO;
    }

    @Override
    public GetArticleByIdVO getArticleById(Long id) {
        Article article = this.getById(id);
        if (article == null){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //以VO返回
        GetArticleByIdVO getArticleByIdVO = BeanCopyUtils.copyBean(article, GetArticleByIdVO.class);

        //查询该文章下对应的标签(返回对应的标签id即可)
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> articleTagList = articleTagService.list(articleTagLambdaQueryWrapper);
        List<Long> tageIdList = articleTagList.stream().map(ArticleTag::getTagId).collect(Collectors.toList());

        getArticleByIdVO.setTags(tageIdList);
        return getArticleByIdVO;
    }

    @Override
    @Transactional
    public void updateArticle(UpdateArticleDto updateArticleDto) {
        //先修改文章信息
        Article article = BeanCopyUtils.copyBean(updateArticleDto, Article.class);
        boolean b = this.updateById(article);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //接着更新标签信息
        //删除原有的 标签-文章关联表
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, article.getId());
        boolean remove = articleTagService.remove(articleTagLambdaQueryWrapper);
        if (!remove){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //按照这次修改的 新增标签-文章关联表
        List<ArticleTag> articleTagList = updateArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        boolean b1 = articleTagService.saveBatch(articleTagList);
        if (!b1){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public ResponseResult deleteArticleById(Long id) {
        boolean b = this.removeById(id);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }


    @NotNull //根据文章的id 从redis中获取文章的浏览量
    private Long getViewCountVO(Long id) {
        Long viewCountVO =Long.valueOf(redisCache.getCacheMap(Redis_ARTICLE_VIEWCOUNT).get(id.toString()).toString());
        if (viewCountVO < 0){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return viewCountVO;
    }
}




