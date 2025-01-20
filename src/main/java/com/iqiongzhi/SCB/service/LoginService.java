package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.dto.LoginDTO;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.UserMapper;
import com.iqiongzhi.SCB.utils.*;
import com.iqiongzhi.SCB.config.Env;

import kong.unirest.Unirest;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.alibaba.fastjson2.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.iqiongzhi.SCB.utils.JWTUtil.REFRESH_SECRET_KEY;

@Service
public class LoginService {
    @Autowired
    UserMapper userMapper;

    /**
     * 山东大学统一认证登录
     * @param SDUId 学号
     * @param password 密码
     * @return Result
     */
    public ResponseEntity<Result> SDUIdentify(String SDUId, String password) {
        String baseURL = "https://pass.sdu.edu.cn/";
        String ticket, sTicket, validationResult;

        //第一次请求(POST)，获取ticket，认证失败返回403
        try {
            ticket = Unirest.post(baseURL + "cas/restlet/tickets").
                    body("username=" + SDUId + "&password=" + password).asString().getBody();
            if (!ticket.startsWith("TGT")) {
                throw new RuntimeException("Ticket wrong");
            }
        } catch (RuntimeException e) {
            return ResponseUtil.build(Result.error(401, "Authentication failed"));
        }

        //第二次请求(POST)，提供服务类型，获取sTicket
        sTicket = Unirest.post(baseURL + "cas/restlet/tickets/" + ticket).
                body("service=https://service.sdu.edu.cn/tp_up/view?m=up").asString().getBody();

        //第三次请求(GET)，提供服务类型和对应的票据，获取用户信息
        validationResult = Unirest.get(baseURL + "cas/serviceValidate").
                queryString("ticket", sTicket).
                queryString("service", "https://service.sdu.edu.cn/tp_up/view?m=up").asString().getBody();

        //提取用户信息
        String username = Regex.getContext(validationResult, "cas:USER_NAME");
        String idType = Regex.getContext(validationResult, "cas:ID_TYPE");
        String KSH = Regex.getContext(validationResult, "cas:KSH");

        if(!isExisted(SDUId)){
            userMapper.addUser(username,password,SDUId);
        }

        String id = Integer.toString(userMapper.getUserId(SDUId, "SDUId"));

        return getToken(id);
    }

    private boolean isExisted(String stuId){
        List<User> list = userMapper.findUserByStuId(stuId);
        return !list.isEmpty();
    }

    private boolean isWxExisted(String openid){
        List<User> list = userMapper.findUserByOpenId(openid);
        return !list.isEmpty();
    }

    private final static String appId = Env.appId;
    private final static String appSecret = Env.appSecret;
    private final static String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 微信登录
     * @param code 微信小程序登录凭证
     * @return Result
     */
    public ResponseEntity<Result> appWxLogin(String code) {
        String url = LOGIN_URL + "?appid=" + appId + "&secret=" + appSecret +
                "&js_code=" + code + "&grant_type=authorization_code";
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
//        LoginDTO是自己定义的,用于接受返回值的json形式
        LoginDTO loginDTO = new LoginDTO();
        try {
//            获取返回值并对其进行json化传给loginDTO
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            loginDTO = JSON.parseObject(html, LoginDTO.class);
//            如果成功就不会有errcode,此处是验证是否成功
            if(null == loginDTO.errcode) {
                Result re  =  Result.error(Integer.valueOf(loginDTO.errcode), loginDTO.errmsg);
                return ResponseUtil.build(re);
            }
        } catch (Exception e) {
            System.out.println("wx login succeed");
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("openid",loginDTO.openid);
        map.put("session_key",loginDTO.session_key);
        if(!isWxExisted(loginDTO.openid)){
            userMapper.addWXUser(loginDTO.openid);
        }
        String user_id = loginDTO.openid;
        String id = Integer.toString(userMapper.getUserId(user_id, "wechat"));

        return getToken(id);

    }

    /**
     * 刷新token
     * @param refreshToken 刷新用token
     * @return Result
     */
    public ResponseEntity<Result> refresh(String refreshToken) {
        try {
            DecodedJWT info = JWTUtil.getTokenInfo(refreshToken, REFRESH_SECRET_KEY);
            String id = info.getClaim("user_id").asString();
            Date date = info.getExpiresAt();
            if (date.after(new Date())) {
                String newAccessToken = JWTUtil.getTokenWithPayLoad(id, JWTUtil.EXPIRE_TIME, JWTUtil.SECRET_KEY);
                Map<String, String> map = new HashMap<>();
                map.put("accessToken", newAccessToken);
                String msg;
                msg = "成功！已获取新accessToken";
                return ResponseUtil.build(new Result(200, map, msg));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseUtil.build(Result.error(400, "无有效RefreshToken"));
    }

    /**
     * 用户名密码登录
     * @param identifier 用户名或邮箱
     * @param password 密码
     * @return Result
     */
    public ResponseEntity<Result> simpleLogin(String identifier, String password) {
        if (Objects.equals(userMapper.getPasswdByName(identifier), password)) {
            String id = Integer.toString(userMapper.getUserId(identifier, "username"));
            return getToken(id);
        } else if (Objects.equals(userMapper.getPasswdByEmail(identifier), password)) {
            String id = Integer.toString(userMapper.getUserId(identifier, "email"));
            return getToken(id);
        } else {
            return ResponseUtil.build(Result.error(401, "账号或密码错误"));
        }
    }

    /**
     * 获取token
     * @param id 用户id
     * @return Result
     */
    private ResponseEntity<Result> getToken(String id) {
        try{
            String refreshToken = JWTUtil.getTokenWithPayLoadWX(id,JWTUtil.REFRESH_EXPIRE_TIME, REFRESH_SECRET_KEY);

            String token = JWTUtil.getTokenWithPayLoadWX(id, JWTUtil.EXPIRE_TIME, JWTUtil.SECRET_KEY);

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("accessToken", token);
            tokenMap.put("refreshToken", refreshToken);

            return ResponseUtil.build(Result.success(tokenMap, "登陆成功"));
        } catch (Exception e){
            return ResponseUtil.build(Result.error(400, e.getMessage()));
        }
    }
}
