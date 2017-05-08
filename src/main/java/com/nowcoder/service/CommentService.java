package com.nowcoder.service;

import com.nowcoder.dao.CommentDao;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chen on 07/05/2017.
 */
@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    public void addComment(Comment comment) {
        commentDao.addComment(comment);
    }

    public List<Comment> selectCommentByEntity(int entityId, int entityType) {
        return commentDao.selectByEntity(entityId, entityType);
    }

    public int getCommentCountByEntity(int entityId, int entityType) {
        return commentDao.getCommentCountByEntity(entityId, entityType);
    }

    public void deleteComment(int cid) {
        commentDao.updateStatus(cid, -1);
    }
}
