package com.fengye.controller;


import com.fengye.domain.ResponseResult;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.UserService;
import com.fengye.utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@RestController
public class UploadController {

    @Resource
    private AliOSSUtils aliOSSUtils;

    @Resource
    private UserService userService;


    //云存储OSS
    @PostMapping("/upload")
    public ResponseResult<String> uploadAvatar(MultipartFile img){
        if (img.isEmpty()){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        // 获取文件大小
        long fileSize = img.getSize();
        // 判断文件大小是否超过2MB（2MB=2*1024*1024 bytes）
        if (fileSize > 2 * 1024 * 1024) {
            // 抛出文件大小超过限制的异常
            throw new SystemException(AppHttpCodeEnum.FILE_SIZE_ERROR);
        }
        //对原始文件名进行判断大小。只能上传png或jpg文件
        if (originalFilename != null && !originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")) {
            //AppHttpCodeEnum是我们在fengye-framework写的枚举类，FILE_TYPE_ERROR代表文件类型错误的提示
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        log.info("文件上传, 原始文件名: {}",img.getOriginalFilename());
        //调用阿里云oss工具类进行文件上传
        String url;
        try {
            url = aliOSSUtils.upload(img);
        } catch (IOException e) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        log.info("文件上传完成，文件上传的url为: {}",url);

        //更新数据库中的avatarUrl，该项目 单独上传头像时不更新数据库，而是修改时 把个人信息（包括头像）一起更新
//        User currentUser = SecurityUtils.getLoginUser().getUser();
//        currentUser.setAvatar(url);
//        boolean result = userService.updateById(currentUser);
//        if (!result){
//            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
//        }
        return ResponseResult.okResult(url);
    }
}
