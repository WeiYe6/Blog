package com.fengye.constants;

/**
 * 常量封装类
 * 字面值(代码中的固定值)处理，把字面值都在这里定义成常量
 * @author fengye
 */
public class SystemConstants {

    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    
    /**
     *  文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    
    /**
     * 文章列表当前查询页数
     */
    public static final int ARTICLE_STATUS_CURRENT = 1;

    /**
     * 文章列表每页显示的数据条数
     */
    public static final int ARTICLE_STATUS_SIZE = 10;

    /**
     * 分类表的分类状态为正常状态-0
     */
    public static final String STATUS_NORMAL = "0";

    /**
     * 友链表的审核状态为 审核通过状态-0
     */
    public static final String LINK_STATUS_NORMAL = "0";

    /**
     * 评论区的某条评论是根评论  默认 -1
     */
    public static final String COMMENT_ROOT = "-1";

    /**
     * 文章的评论
     */
    public static final String ARTICLE_COMMENT = "0";

    /**
     * 友链的评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     * 文章浏览量的 redisKey键
     */
    public static final  String  Redis_ARTICLE_VIEWCOUNT="article:viewCount";
    /**
     * 权限类型，菜单
     */
    public static final String TYPE_MENU = "C";

    /**
     * 权限类型，按钮
     */
    public static final String TYPE_BUTTON = "F";

    /**
     * 判断为管理员用户
     */
    public static final String IS_ADMIN = "1";


}