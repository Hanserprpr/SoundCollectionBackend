package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.po.LoginDTO;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.UserMapper;
import com.iqiongzhi.SCB.utils.JWTUtil;
import com.iqiongzhi.SCB.utils.Regex;
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
import org.springframework.stereotype.Service;

import java.util.*;

import static com.iqiongzhi.SCB.utils.JWTUtil.REFRESH_SECRET_KEY;

@Service
public class LoginService {
    @Autowired
    UserMapper userMapper;
    public Result SDUIdentify(String SDUId, String password) {
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
            return Result.error(401, "Authentication failed");
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

        try{
            String refreshToken = JWTUtil.getTokenWithPayLoad(SDUId,JWTUtil.REFRESH_EXPIRE_TIME, REFRESH_SECRET_KEY);

            String token = JWTUtil.getTokenWithPayLoad( SDUId, JWTUtil.EXPIRE_TIME, JWTUtil.SECRET_KEY);

            Map<String, String> map = new HashMap<>();
            map.put("accessToken", token);
            map.put("refreshToken", refreshToken);
            System.out.println("app login succeed");
            return Result.success(map, "登陆成功");
        } catch (Exception e){
            return Result.error(400, e.getMessage());
        }
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

    public Result appWxLogin(String code) {
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
                return re;
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
        try{
            String refreshToken = JWTUtil.getTokenWithPayLoadWX( user_id,JWTUtil.REFRESH_EXPIRE_TIME, REFRESH_SECRET_KEY);

            String token = JWTUtil.getTokenWithPayLoadWX(user_id, JWTUtil.EXPIRE_TIME, JWTUtil.SECRET_KEY);

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("accessToken", token);
            tokenMap.put("refreshToken", refreshToken);

            return Result.success(tokenMap, "登陆成功");
        } catch (Exception e){
            return Result.error(400, e.getMessage());
        }


//        后续开发的注意事项,此处的openid是所有微信用户唯一的
//        可以在数据库中独立出微信用户的表,并用openid进行索引和查找
    }

    public Result refresh(String refreshToken) {
        try {
            System.out.println("start");
            DecodedJWT info = JWTUtil.getTokenInfo(refreshToken, REFRESH_SECRET_KEY);
            String SDUId = info.getClaim("user_id").asString();
            Date date = info.getExpiresAt();
            if (date.after(new Date())) {
                String newAccessToken = JWTUtil.getTokenWithPayLoad(SDUId, JWTUtil.EXPIRE_TIME, JWTUtil.SECRET_KEY);
                Map<String, String> map = new HashMap<>();
                map.put("accessToken", newAccessToken);
                String msg;
                msg = "成功！已获取新accessToken";
                return new Result(200, map, msg);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Result.error(400, "无有效RefreshToken");
    }


}
