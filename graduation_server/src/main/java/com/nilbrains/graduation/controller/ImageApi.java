package com.nilbrains.graduation.controller;

import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ImageApi {

    @Autowired
    private IImageService imageService;

    //  上传图片
    @PreAuthorize("@permission.login()")
    @PostMapping("/image")
    public ResponseResult uploadImage(@RequestParam("file") MultipartFile file) {
        return imageService.uploadImage(file);
    }

    //    获取图片
    @GetMapping("image/{imageId}")
    public void viewImage(HttpServletResponse response, @PathVariable String imageId) throws IOException {
        imageService.viewImage(response, imageId);
    }

}
