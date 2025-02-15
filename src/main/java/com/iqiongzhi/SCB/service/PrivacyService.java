package com.iqiongzhi.SCB.service;


import com.iqiongzhi.SCB.mapper.PrivacyMapper;
import com.iqiongzhi.SCB.utils.ResponseUtil;
import com.iqiongzhi.SCB.data.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
public class PrivacyService {
    @Autowired
    private PrivacyMapper followMapper;
    @Autowired
    private PrivacyMapper privacyMapper;

    public ResponseEntity<Result> hideFollows(String userId) {
        if(followMapper.getFollowsPriById(userId)==1){
            followMapper.hideFollows(userId);
            return ResponseUtil.build(Result.ok());
        } else {
            return ResponseUtil.build(Result.error(400, "隐藏关注失败qwq"));
        }
    }



    public ResponseEntity<Result> hideFans(String userId) {
        if(followMapper.getFansPriById(userId)==1) {
            followMapper.hideFans(userId);
            return ResponseUtil.build(Result.ok());
        } else{
            return ResponseUtil.build(Result.error(400, "隐藏粉丝失败qwq"));
        }
    }

    public ResponseEntity<Result> hideCollections(String userId) {
        if(followMapper.getCollectionsPriById(userId)==1) {
            followMapper.hideCollections(userId);
            return ResponseUtil.build(Result.ok());
        } else{
            return ResponseUtil.build(Result.error(400, "隐藏收藏夹失败qwq"));
        }
    }

    public ResponseEntity<Result> displayFollows(String userId) {
        if(followMapper.getFollowsPriById(userId)==0){
            followMapper.displayFollows(userId);
            return ResponseUtil.build(Result.ok());
        } else {
            return ResponseUtil.build(Result.error(400, "显示关注失败qwq"));
        }
    }



    public ResponseEntity<Result> displayFans(String userId) {
        if(followMapper.getFansPriById(userId)==0) {
            followMapper.displayFans(userId);
            return ResponseUtil.build(Result.ok());
        } else{
            return ResponseUtil.build(Result.error(400, "显示粉丝失败qwq"));
        }
    }

    public ResponseEntity<Result> displayCollections(String userId) {
        if(followMapper.getCollectionsPriById(userId)==0) {
            followMapper.displayCollections(userId);
            return ResponseUtil.build(Result.ok());
        } else{
            return ResponseUtil.build(Result.error(400, "显示收藏夹失败qwq"));
        }
    }

    public ResponseEntity<Result> getFollowsPri(String userId) {
        try {
                return ResponseUtil.build(Result.success(privacyMapper.getFollowsPriById(userId), "获取关注列表隐私设置1/0"));
        }catch (DataIntegrityViolationException e) {
            return ResponseUtil.build(Result.error(404, "用户不存在"));
        }catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取失败"));
        }
    }

    public ResponseEntity<Result> getFansPri(String userId) {
        try {
            return ResponseUtil.build(Result.success(privacyMapper.getFansPriById(userId), "获取粉丝列表隐私设置1/0"));
        }catch (DataIntegrityViolationException e) {
            return ResponseUtil.build(Result.error(404, "用户不存在"));
        }catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取失败"));
        }
    }

    public ResponseEntity<Result> getCollectionsPri(String userId) {
        try {
            return ResponseUtil.build(Result.success(privacyMapper.getCollectionsPriById(userId), "获取收藏列表隐私设置1/0"));
        }catch (DataIntegrityViolationException e) {
            return ResponseUtil.build(Result.error(404, "用户不存在"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(400, "获取失败"));
        }
    }
}
