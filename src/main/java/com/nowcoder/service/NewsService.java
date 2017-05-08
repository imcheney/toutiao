package com.nowcoder.service;

import com.nowcoder.dao.NewsDao;
import com.nowcoder.model.News;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by Chen on 04/05/2017.
 */
@Service
public class NewsService {
    @Autowired
    NewsDao newsDao;

    public List<News> getLatestNews(int uid, int offset, int limit) {
        return newsDao.selectByUidAndOffset(uid, offset, limit);
    }

    public String saveImage(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        int dotPos = filename.lastIndexOf('.');
        if (dotPos == -1) {  //illegal
            return null;
        }
        String ext = filename.substring(dotPos+1).toLowerCase();
        if (!ToutiaoUtil.checkImageFormat(ext)) {  //illegal
            return null;
        } else {
            String newFilename = UUID.randomUUID().toString().replaceAll("-", "0") + "." + ext;
            Files.copy(file.getInputStream(), (new File(ToutiaoUtil.IMAGE_DIR+ newFilename)).toPath(), StandardCopyOption.REPLACE_EXISTING);
            return ToutiaoUtil.TOUTIAO_DOMAIN + "image?filename=" + newFilename;
        }
    }

    public void addNews(News news) {
        newsDao.addNews(news);
    }

    public News getNewsByNid(int nid) {
        return newsDao.selectByNid(nid);
    }

    public void updateCommentCount(int count, int nid) {
        newsDao.updateCommentCount(count, nid);
    }
}
