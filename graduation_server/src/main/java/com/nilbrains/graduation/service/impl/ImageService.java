package com.nilbrains.graduation.service.impl;

import com.nilbrains.graduation.dao.ImageMapper;
import com.nilbrains.graduation.pojo.Image;
import com.nilbrains.graduation.pojo.User;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.IImageService;
import com.nilbrains.graduation.service.IUserService;
import com.nilbrains.graduation.utils.Constants;
import com.nilbrains.graduation.utils.IdWorker;
import com.nilbrains.graduation.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class ImageService implements IImageService {


    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

    @Value("${nil.image.save-path}")
    public String imagePath;

    @Value("${nil.image.max-size}")
    public long maxSize;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private IUserService userService;

    @Override
    public ResponseResult uploadImage(MultipartFile file) {
        log.info("path == > " + imagePath);
        //判断是否有文件
        if (file == null) {
            return ResponseResult.FAILED("图片不可以为空.");
        }
        //判断文件类型，我们只支持图片上传，比如说：png，jpg，gif
        String contentType = file.getContentType();
        log.info("contentType == > " + contentType);
        if (TextUtils.isEmpty(contentType)) {
            return ResponseResult.FAILED("图片格式错误.");
        }
        //获取相关数据，比如说文件类型，文件名称
        String originalFilename = file.getOriginalFilename();
        log.info("originalFilename == > " + originalFilename);
        String type = getType(contentType, originalFilename);
        if (type == null) {
            return ResponseResult.FAILED("不支持此图片类型");
        }
        //限制文件的大小
        long size = file.getSize();
        log.info("maxSize === > " + maxSize + "  size ==> " + size);
        if (size > maxSize) {
            return ResponseResult.FAILED("图片最大仅支持" + (maxSize / 1024 / 1024) + "Mb");
        }
        //创建图片的保存目录
        //规则：配置目录/日期/类型/ID.类型
        long currentMillions = System.currentTimeMillis();
        String currentDay = simpleDateFormat.format(currentMillions);
        log.info("current day == > " + currentDay);
        String dayPath = imagePath + File.separator + currentDay;
        File dayPathFile = new File(dayPath);
        //判断日期文件夹是否存在//2020_06_26
        if (!dayPathFile.exists()) {
            dayPathFile.mkdirs();
        }
        String targetName = String.valueOf(idWorker.nextId());
        String targetPath = dayPath +
                File.separator + type + File.separator + targetName + "." + type;
        File targetFile = new File(targetPath);
        //判断类型文件夹是否存在//gif
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        log.info("targetFile ---- == > " + targetFile);

        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            log.info("targetFile == > " + targetFile);
            //保存文件
            file.transferTo(targetFile);
            //返回结果：包含这个图片的名称和访问路径
            //第一个是访问路径 -- > 得对应着解析来
            Map<String, String> result = new HashMap<>();
            String resultPath = currentMillions + "_" + targetName + "." + type;
            result.put("id", resultPath);
            result.put("url", Constants.Setting.G_PATH + resultPath);
            //第二个是名称--->alt="图片描述",如果不写，前端可以使用名称作为这个描述
            result.put("name", originalFilename);
            Image image = new Image();
            image.setContentType(contentType);
            image.setId(targetName);
            image.setCreateTime(new Date());
            image.setPath(targetFile.getPath());
            image.setName(originalFilename);
            image.setUrl(resultPath);
            image.setStatus("0");
            User user = userService.checkUser();
            image.setUid(user.getUid());
            //记录文件
            //保存记录到数据里
            imageMapper.save(image);
            //返回结果
            return ResponseResult.SUCCESS("文件上传成功").setData(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.FAILED("图片上传失败，请稍后重试");
    }

    @Override
    public void viewImage(HttpServletResponse response, String imageId) throws IOException {
        log.info("imageId == > " + imageId);
        String[] paths = imageId.split("_");
//        images ID
        String name = paths[1];
        String imageIdDb = name.split("\\.")[0];
        Image image = imageMapper.getSimpleOne(imageIdDb);
        if (image == null) {
            return;
        }
        String targetPath = image.getPath();
        log.info("targetPath == >" + targetPath);
        File file = new File(targetPath);
        OutputStream writer = null;
        FileInputStream ips = null;
        try {
            ips = new FileInputStream(file);
//            返回类型
            response.setContentType(image.getContentType());
            writer = response.getOutputStream();
            //读取文件流
            int len;
            byte[] buffer = new byte[1024];
            while ((len = ips.read(buffer)) != -1) {
                writer.write(buffer, 0, len);
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert ips != null;
            ips.close();
            assert writer != null;
            writer.close();
        }
    }


    private String getType(String contentType, String name) {
        String type = null;
        if (Constants.ImageType.TYPE_PNG_WITH_PREFIX.equals(contentType) &&
                name.endsWith(Constants.ImageType.TYPE_PNG)) {
            type = Constants.ImageType.TYPE_PNG;
        } else if (Constants.ImageType.TYPE_GIF_WITH_PREFIX.equals(contentType) &&
                name.endsWith(Constants.ImageType.TYPE_GIF)) {
            type = Constants.ImageType.TYPE_GIF;
        } else if ((Constants.ImageType.TYPE_JPG_WITH_PREFIX.equals(contentType) ||
                Constants.ImageType.TYPE_JPEG_WITH_PREFIX.equals(contentType)) &&
                name.endsWith(Constants.ImageType.TYPE_JPG)) {
            type = Constants.ImageType.TYPE_JPG;
        }
        return type;
    }

}
