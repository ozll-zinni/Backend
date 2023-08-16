package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.entity.Heart;
import com.example.traveler.model.entity.Post;
import com.example.traveler.model.entity.Scrap;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.HeartRepository;
import com.example.traveler.repository.ScrapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
public class ScrapService {
    @Autowired
    private ScrapRepository scrapRepository;

    public Scrap saveScrap(User user, Post post) throws BaseException {

        Scrap saveScrap = null;

        try {
            Scrap newScrap = new Scrap(user, post);
            saveScrap = scrapRepository.save(newScrap);
        } catch (Exception e) {
            throw new BaseException(POST_SCRAP_FAIL);
        }
        return saveScrap;
    }
    public Scrap getScrap(User user, Post post) throws BaseException {
        try {
            return scrapRepository.findByUserAndPost(user, post);
        } catch (Exception e) {
            throw new BaseException(POST_SCRAP_GET_FAIL);
        }
    }

    public int deleteScrap(Scrap scrap) throws BaseException {
        try {
            scrapRepository.delete(scrap);
        } catch (Exception e) {
            throw new BaseException(POST_SCRAP_CANCEL_FAIL);
        }
        return 1;
    }

    public int countScrap(Post post) throws BaseException {
        try {
            return (int) scrapRepository.countByPost(post);
        } catch (Exception e) {
            throw new BaseException(POST_SCRAP_COUNT_FAIL);
        }
    }
}
