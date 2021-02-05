package com.nilbrains.graduation.service.impl;

import com.nilbrains.graduation.dao.PostMapper;
import com.nilbrains.graduation.pojo.Post;
import com.nilbrains.graduation.pojo.User;
import com.nilbrains.graduation.response.PostRecommend;
import com.nilbrains.graduation.response.PostWithUser;
import com.nilbrains.graduation.response.ResponsePage;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.IPostService;
import com.nilbrains.graduation.service.IUserService;
import com.nilbrains.graduation.service.IUserinfoService;
import com.nilbrains.graduation.utils.Constants;
import com.nilbrains.graduation.utils.IdWorker;
import com.nilbrains.graduation.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class PostService implements IPostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserinfoService userinfoService;

    private Random random = new Random();

    @Override
    public ResponseResult addPost(String type, Post post) {
        User user = userService.checkUser();
//        添加文章
        if (TextUtils.isEmpty(post.getTitle())) {
            return ResponseResult.FAILED("标题不能为空");
        }
        if (TextUtils.isEmpty(post.getCid())) {
            return ResponseResult.FAILED("分类不能为空");
        }
        if (TextUtils.isEmpty(post.getContent())) {
            return ResponseResult.FAILED("内容不能为空");
        }
        if (TextUtils.isEmpty(post.getDesc())) {
            return ResponseResult.FAILED("描述不能为空");
        }
//        补全数据
        post.setPid(String.valueOf(idWorker.nextId()));
        post.setUid(user.getUid());
        post.setType(type);
//      -  cover
        post.setStatus(Constants.Status.ACTIVE);
        post.setViews(Constants.Status.ZERO);
        post.setPublishTime(new Date());

        postMapper.save(post);
//        积分
        if (Constants.Status.ARTICLE.equals(type)) {
            userinfoService.addIntegral(user.getUid(), Constants.Userinfo.PY_4);
        } else if (Constants.Status.ANSWER.equals(type)) {
            userinfoService.addIntegral(user.getUid(), Constants.Userinfo.PY_2);
        }
        return ResponseResult.SUCCESS("添加成功");
    }

    @Override
    public ResponseResult simpleInfo(String pid) {

        Post post = postMapper.getSimpleOne(pid);

        if (post == null) {
            return ResponseResult.ERROR_404();
        }
        postMapper.addViews(pid);
        return ResponseResult.SUCCESS("获取成功").setData(post);
    }

    @Override
    public ResponseResult info(String pid) {
        PostWithUser post = postMapper.getOne(pid);

        if (post == null) {
            return ResponseResult.ERROR_404();
        }
        postMapper.addViews(pid);

        post.setViews(post.getViews() + 1);
        
        return ResponseResult.SUCCESS("获取成功").setData(post);
    }

    @Override
    public ResponseResult modify(Post post) {

        User user = userService.checkUser();

        if (TextUtils.isEmpty(post.getPid())) {
            return ResponseResult.FAILED("ID不能为空");
        }
        if (Constants.User.ROLE_USER.equals(user.getRole()) && !user.getUid().equals(post.getUid())) {
            return ResponseResult.ERROR_403();
        }

        if (TextUtils.isEmpty(post.getTitle())) {
            return ResponseResult.FAILED("标题不能为空");
        }
        if (TextUtils.isEmpty(post.getCid())) {
            return ResponseResult.FAILED("分类不能为空");
        }
        if (TextUtils.isEmpty(post.getContent())) {
            return ResponseResult.FAILED("内容不能为空");
        }

        postMapper.updatePost(post);

        return ResponseResult.SUCCESS("修改成功");
    }

    @Override
    public ResponseResult delete(String pid) {
        User user = userService.checkUser();
        Post post = postMapper.getSimpleOne(pid);
        if (post == null) {
            return ResponseResult.ERROR_404();
        }
        if (Constants.User.ROLE_USER.equals(user.getRole()) && !user.getUid().equals(post.getUid())) {
            return ResponseResult.ERROR_403();
        }
        String status = post.getStatus();
        if (Constants.Status.ACTIVE.equals(status)) status = Constants.Status.INACTIVE;
        else if (Constants.Status.INACTIVE.equals(status)) status = Constants.Status.ACTIVE;
        postMapper.delete(pid, status);

        return ResponseResult.SUCCESS("操作成功");
    }

    @Override
    public ResponseResult list(
            String title,
            String cid,
            String type,
            String answer,
            String status,
            int currentPage,
            int pageSize
    ) {
        log.info("status === >" + status);
        int totalCount = postMapper.count(title, cid, type, "", answer, status);
        ResponsePage responsePage = new ResponsePage();
        responsePage.setTotalCount(totalCount);
        responsePage.setCurrentPage(currentPage);
        responsePage.setPageSize(pageSize);
        responsePage.setCountPage((int) Math.ceil(1.0 * totalCount / pageSize));
        if (totalCount == 0) {
            return ResponseResult.SUCCESS("没有数据了").setData(responsePage);
        }
        List<PostWithUser> list = postMapper.list(title, cid, type, "", answer, status, pageSize * (currentPage - 1), pageSize);

        responsePage.setPageData(list);

        return ResponseResult.SUCCESS("获取成功").setData(responsePage);
    }

    @Override
    public ResponseResult list(String type, String answer, String status, int currentPage, int pageSize) {
        User user = userService.checkUser();
        String uid = user.getUid();
        return getResponseResult(uid, type, answer, status, currentPage, pageSize);
    }

    @Override
    public ResponseResult changeAnswer(String pid) {
        User user = userService.checkUser();
        Post post = postMapper.getSimpleOne(pid);
        if (post == null) {
            return ResponseResult.ERROR_404();
        }
        if (Constants.User.ROLE_USER.equals(user.getRole()) && !user.getUid().equals(post.getUid())) {
            return ResponseResult.ERROR_403();
        }
        String answer = post.getAnswer();
        if (Constants.Status.ACTIVE.equals(answer)) answer = Constants.Status.INACTIVE;
        else if (Constants.Status.INACTIVE.equals(answer)) answer = Constants.Status.ACTIVE;
        postMapper.changeAnswer(pid, answer);

        return ResponseResult.SUCCESS("操作成功");
    }

    @Override
    public ResponseResult listRecommendPost(String pid, int size) {
//        获取推荐文章
//        根据文章id 获取文章标签
        String tags = postMapper.getTagsByPid(pid);
        //        拆分标签 ('-')
        List<String> tagList = new ArrayList<>();
        List<PostRecommend> likeResultList = new ArrayList<>();
        if (!TextUtils.isEmpty(tags)) {
            if (!tags.contains("-")) {
                tagList.add(tags);
            } else {
                tagList.addAll(Arrays.asList(tags.split("-")));
            }
            //        从中随机获取一标签，查询与此标签相似的文章
            String targetTag = tagList.get(random.nextInt(tagList.size()));

            log.info("targetTag == > " + targetTag);

            likeResultList.addAll(postMapper.listPostByLikeTag("%" + targetTag + "%", size));
        }

        if (likeResultList.size() < size) {
            //说明不够数量，获取最新的文章作为补充
            int dxSize = size - likeResultList.size();
            List<PostRecommend> dxList = postMapper.listLastedPostBySize(dxSize);
            //这个写法有一定的弊端，会把可能前面找到的也加进来，概率比较小，如果文章比较多
            likeResultList.addAll(dxList);
        }

        return ResponseResult.SUCCESS("获取推荐文章成功.").setData(likeResultList);
    }

    @Override
    public ResponseResult listByUser(String uid, String type, String answer, String status, int currentPage, int pageSize) {
        return getResponseResult(uid, type, answer, status, currentPage, pageSize);
    }

    private ResponseResult getResponseResult(String uid, String type, String answer, String status, int currentPage, int pageSize) {
        int totalCount = postMapper.count("", "", type, uid, answer, status);
        ResponsePage responsePage = new ResponsePage();
        responsePage.setTotalCount(totalCount);
        responsePage.setCurrentPage(currentPage);
        responsePage.setPageSize(pageSize);
        responsePage.setCountPage((int) Math.ceil(1.0 * totalCount / pageSize));
        if (totalCount == 0) {
            return ResponseResult.SUCCESS("没有数据了").setData(responsePage);
        }
        List<PostWithUser> list = postMapper.list("", "", type, uid, answer, status, pageSize * (currentPage - 1), pageSize);
        responsePage.setPageData(list);

        return ResponseResult.SUCCESS("获取成功").setData(responsePage);
    }
}
