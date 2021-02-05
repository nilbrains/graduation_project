package com.nilbrains.graduation.service;

import com.nilbrains.graduation.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IImageService {
    /**
     * 上传 图片 文件
     *
     * @param file
     * @return
     */
    ResponseResult uploadImage(MultipartFile file);

    /**
     * 向客户端 回显图片
     * <p>类似： admin_picture.jpg</p>
     *
     * @param response
     * @param imageId  图片的id
     * @throws IOException
     */
    void viewImage(HttpServletResponse response, String imageId) throws IOException;
}
