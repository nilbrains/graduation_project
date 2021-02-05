package com.nilbrains.graduation.service.impl;

import com.nilbrains.graduation.dao.CommentMapper;
import com.nilbrains.graduation.pojo.Comment;
import com.nilbrains.graduation.pojo.User;
import com.nilbrains.graduation.response.CommentWithUser;
import com.nilbrains.graduation.response.ResponsePage;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.ICommentService;
import com.nilbrains.graduation.service.IUserService;
import com.nilbrains.graduation.service.IUserinfoService;
import com.nilbrains.graduation.utils.Constants;
import com.nilbrains.graduation.utils.IdWorker;
import com.nilbrains.graduation.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CommentService implements ICommentService {
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserinfoService userinfoService;

    @Override
    public ResponseResult add(String pid, Comment comment) {
        User user = userService.checkUser();

        if (TextUtils.isEmpty(comment.getContent())) {
            return ResponseResult.FAILED("评论内容不能为空");
        }
        comment.setId(String.valueOf(idWorker.nextId()));
        comment.setUid(user.getUid());
        comment.setPid(pid);
        comment.setIsTrue(Constants.Status.FALSE);
        comment.setPublishTime(new Date());
        if (!TextUtils.isEmpty(comment.getTid())) {
            Comment commentDB = commentMapper.getOneById(comment.getTid());
            if (commentDB != null) {
                comment.setTid(comment.getTid());
            }
        }
        commentMapper.save(comment);
        userinfoService.addIntegral(user.getUid(), Constants.Userinfo.PY_2);
        return ResponseResult.SUCCESS("提交成功");
    }

    @Override
    public ResponseResult delete(String id) {
//        待定
        commentMapper.delete(id);
        commentMapper.deleteChild(id);
        return ResponseResult.SUCCESS("删除成功");
    }

    @Override
    public ResponseResult list(String pid, int currentPage, int pageSize) {

        int totalCount = commentMapper.getCount(pid);
        ResponsePage responsePage = new ResponsePage();
        responsePage.setTotalCount(totalCount);
        responsePage.setCurrentPage(currentPage);
        responsePage.setPageSize(pageSize);
        responsePage.setCountPage((int) Math.ceil(1.0 * totalCount / pageSize));
        if (totalCount == 0) {
            return ResponseResult.SUCCESS("没有数据了").setData(responsePage);
        }
        List<CommentWithUser> comments = commentMapper.findCommentsByPid(pid, pageSize * (currentPage - 1), pageSize);

        for (CommentWithUser comment : comments) {
            comment.setChildren(commentMapper.findCommentsByTid(comment.getId()));
        }
        responsePage.setPageData(comments);
        return ResponseResult.SUCCESS("获取成功").setData(responsePage);
    }

    @Override
    public ResponseResult setTrue(String id) {
        Comment commentDB = commentMapper.getOneById(id);
        String isTrue = commentDB.getIsTrue();
        if (Constants.Status.TRUE.equals(isTrue)) isTrue = Constants.Status.FALSE;
        else if (Constants.Status.FALSE.equals(isTrue)) isTrue = Constants.Status.TRUE;
        commentMapper.updateTrue(id, isTrue);
        return ResponseResult.SUCCESS("设置成功");
    }

    @Override
    public ResponseResult list(int currentPage, int pageSize) {
        int totalCount = commentMapper.getCountAll();
        ResponsePage responsePage = new ResponsePage();
        responsePage.setTotalCount(totalCount);
        responsePage.setCurrentPage(currentPage);
        responsePage.setPageSize(pageSize);
        responsePage.setCountPage((int) Math.ceil(1.0 * totalCount / pageSize));
        if (totalCount == 0) {
            return ResponseResult.SUCCESS("没有数据了").setData(responsePage);
        }
        List<CommentWithUser> comments = commentMapper.findComments(pageSize * (currentPage - 1), pageSize);

        responsePage.setPageData(comments);
        return ResponseResult.SUCCESS("获取成功").setData(responsePage);
    }
}
