package com.fengye.domain.vo;
import lombok.Data;

/**
 * 友链VO封装类
 * @author fegnye
 */
@Data
public class LinkVO {
    //友链id
    private Long id;
    //友链名称
    private String name;
    //友链logo
    private String logo;
    //友链描述
    private String description;
    //网站地址
    private String address;



}
