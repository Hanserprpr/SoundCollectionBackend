package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.dto.LoginDTO;
import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.mapper.PrivacyMapper;
import com.iqiongzhi.SCB.mapper.UserMapper;
import com.iqiongzhi.SCB.utils.*;
import com.iqiongzhi.SCB.utils.BcryptUtils;

import kong.unirest.Unirest;
import com.alibaba.fastjson2.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.iqiongzhi.SCB.utils.JWTUtil.REFRESH_SECRET_KEY;

@Service
public class LoginService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PrivacyMapper privacyMapper;

    /**
     * 山东大学统一认证登录
     * @param SDUId 学号
     * @param password 密码
     * @return ResponseEntity<Result>
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
            String passwd = BcryptUtils.encrypt(password);
            userMapper.addUser(username, passwd,SDUId);
            int id = userMapper.getUserId(SDUId, "SDUId");
            privacyMapper.insertPrivacy(id);
        }

        String id = Integer.toString(userMapper.getUserId(SDUId, "SDUId"));

        return getToken(id);
    }

    private boolean isExisted(String stuId){
        Integer count = userMapper.getUserId(stuId, "SDUId");
        return count != null && count > 0;
    }

    private boolean isWxExisted(String openid){
        List<User> list = userMapper.findUserByOpenId(openid);
        return !list.isEmpty();
    }
    @Value("${wechat.appid}")
    private String appId;

    @Value("${wechat.secret}")
    private String secret;

    private final static String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 微信登录
     * @param code 微信小程序登录凭证
     * @return ResponseEntity<Result>
     */
    public ResponseEntity<Result> appWxLogin(String code) {
        String url = LOGIN_URL + "?appid=" + appId + "&secret=" + secret +
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
                Result re  =  Result.error(400, loginDTO.errmsg);
                return ResponseUtil.build(re);
            }
        } catch (Exception e) {
            System.out.println("wx login succeed");
        }
        if(!isWxExisted(loginDTO.openid)){
            userMapper.addWXUser(loginDTO.openid);
            int id = userMapper.getUserId(loginDTO.openid, "wechat");
            privacyMapper.insertPrivacy(id);
        }
        String user_id = loginDTO.openid;
        String id = Integer.toString(userMapper.getUserId(user_id, "wechat"));

        return getToken(id);

    }

    /**
     * 刷新token
     * @param userId 刷新用token
     * @return ResponseEntity<Result>
     */
    public ResponseEntity<Result> refresh(String userId) {
        try {
             String newAccessToken = jwtUtil.getToken(userId, JWTUtil.EXPIRE_TIME, JWTUtil.SECRET_KEY);
             Map<String, String> map = new HashMap<>();
             map.put("accessToken", newAccessToken);
             String msg;
             msg = "成功！已获取新accessToken";
             return ResponseUtil.build(new Result(200, map, msg));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseUtil.build(Result.error(400, "无有效RefreshToken"));
    }

    /**
     * 用户名密码登录
     * @param identifier 用户名或邮箱
     * @param password 密码
     * @return ResponseEntity<Result>
     */
    public ResponseEntity<Result> simpleLogin(String identifier, String password) {
        String passwd = userMapper.getPasswdByName(identifier);
        if (passwd != null && BcryptUtils.verifyPasswd(password, passwd)) {
            String id = Integer.toString(userMapper.getUserId(identifier, "username"));
            return getToken(id);
        } else if ((passwd = userMapper.getPasswdByEmail(identifier)) != null && BcryptUtils.verifyPasswd(password, passwd)) {
            String id = Integer.toString(userMapper.getUserId(identifier, "email"));
            return getToken(id);
        } else {
            return ResponseUtil.build(Result.error(401, "账号或密码错误"));
        }
    }

    private final Map<String, Map<String, String>> ticketIdMap = new ConcurrentHashMap<>();
    /**
     * 验证码登录
     * @param email 邮箱
     * return ResponseEntity<Result>
     */
    public ResponseEntity<Result> emailLogin(String email) {
        if (userMapper.findUserByEmail(email) == null) {
            return ResponseUtil.build(Result.error(401, "邮箱未注册"));
        }
        String id = Integer.toString(userMapper.getUserId(email, "email"));
        String[] verify = verificationService.generateVerification();
        String code = verify[0];
        String ticket = verify[1];

        Map<String, String> data = new HashMap<>();
        data.put("user_id", id);
        ticketIdMap.put(ticket, data);
        try{
            // 发送邮件
            EmailSender.sendEmail(email, "声音博物馆登录验证码", "您的验证码是：" + code);}
        catch(Exception e){
            return ResponseUtil.build(Result.error(554, "邮件发送失败"));
        }
        return ResponseUtil.build(Result.success(ticket, "验证码已发送"));
    }


    /**
     * 验证邮箱登录
     * @param ticket 获取验证码时返回的 ticket
     * @param code 验证码
     * @return 验证结果
     */
    public ResponseEntity<Result> verify(String ticket, String code) {
        if (verificationService.verifyCode(ticket, code)) {
            Map<String, String> data = ticketIdMap.get(ticket);
            ticketIdMap.remove(ticket);
            return getToken(data.get("user_id"));
        } else {
            return ResponseUtil.build(Result.error(401, "验证码错误"));
        }
    }

    /**
     * 获取token
     * @param id 用户id
     * @return ResponseEntity<Result>
     */
    private ResponseEntity<Result> getToken(String id) {
        String token;
        String refreshToken;
        try {
            refreshToken = jwtUtil.getToken(id, JWTUtil.REFRESH_EXPIRE_TIME, REFRESH_SECRET_KEY);

            token = jwtUtil.getToken(id, JWTUtil.EXPIRE_TIME, JWTUtil.SECRET_KEY);

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("accessToken", token);
            tokenMap.put("refreshToken", refreshToken);
            userMapper.updateLastLoginTime(Integer.parseInt(id));

            return ResponseUtil.build(Result.success(tokenMap, "登陆成功"));
        } catch (Exception e) {
            return ResponseUtil.build(Result.error(500, e.getMessage()));
        }
    }

}
