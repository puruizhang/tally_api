package com.bestzpr.tally.service.component;

import com.bestzpr.tally.config.exception.WeChatServiceException;
import com.bestzpr.tally.domain.dto.WeChatUserInfoDTO;
import com.bestzpr.tally.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @className WechatService
 * @Desc 微信服务
 * @Author 张埔枘
 * @Date 2023/12/22 23:28
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WechatService {

    private final OSSService ossService;
    private final RestTemplate restTemplate;
    @Value("${wx.app-id}")
    private String appId;
    @Value("${wx.app-secret}")
    private String appSecret;

    public WeChatUserInfoDTO getWeChatLoginInfo(String jsCode) throws Exception{
        String url = "https://api.weixin.qq.com/sns/jscode2session";

        // 设置请求参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("appid", appId);
        params.add("secret", appSecret);
        params.add("js_code", jsCode);
        params.add("grant_type", "authorization_code");

        // 发送请求并获取响应
        ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);
        String responseBody = response.getBody();
        log.info("responseBody :{}", responseBody);

        // 将响应体转换为DTO
        WeChatUserInfoDTO weChatUserInfo = null;
        try {
            weChatUserInfo = JsonUtil.fromJson(responseBody, WeChatUserInfoDTO.class);
        } catch (JsonProcessingException e) {
            log.error("getWeChatLoginInfo exception:{}",e.getMessage());
            throw e;
        }

        // 检查是否返回错误码
        if (Objects.nonNull(weChatUserInfo.getErrcode())) {
            log.error("error message {}", weChatUserInfo.getErrcode() + weChatUserInfo.getErrmsg());
            // 抛出更具体的异常可能更好
            throw new WeChatServiceException("Error retrieving wechat info: " + weChatUserInfo.getErrmsg());
        }

        return weChatUserInfo;
    }

    public String getQRCodeUrl(Long roomId){
        String fileName = "roomCode/" + UUID.randomUUID().toString();
        return ossService.uploadByteDataToOSS(fileName,downloadMiniCode(roomId));
    }

    private String getAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+appSecret;

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            // 解析返回的 JSON 数据获取 access_token
            // 这里仅作示例，实际开发中建议使用更健壮的 JSON 解析方式
            String accessToken = parseAccessToken(responseBody);
            return accessToken;
        } else {
            throw new IOException("Failed to get access token");
        }
    }


    private String parseAccessToken(String responseBody) {
        // 解析 JSON 数据获取 access_token
        // 这里使用了一个 JSON 解析库，你可以根据自己的需求选择合适的库
        // 这里仅作示例，实际开发中建议使用更健壮的 JSON 解析方式
        // 假设返回的 JSON 数据格式为 {"access_token":"ACCESS_TOKEN","expires_in":7200}
        // 这里简单地使用字符串截取的方式获取 access_token
        int startIndex = responseBody.indexOf("\"access_token\":\"") + 16;
        int endIndex = responseBody.indexOf("\",\"expires_in\"");
        return responseBody.substring(startIndex, endIndex);
    }

    /**
     * 某带参数的小程序二维码
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.getUnlimited.html
     * by zhengkai.blog.csdn.net
     *
     * @param certNumber 带过去小程序的参数，一般为你的业务参数，建议是id或者number
     * @return 图片的byte数组
     */
    public byte[] downloadMiniCode(Long certNumber) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scene", certNumber);
        paramMap.put("is_hyaline", true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            URL url = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + getAccessToken());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10000); // 连接超时 单位毫秒
            httpURLConnection.setReadTimeout(10000); // 读取超时 单位毫秒
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            printWriter.write(JsonUtil.toJson(paramMap)); // 这里需要一个将Map转换为JSON字符串的工具类
            printWriter.flush();
            printWriter.close();

            // 开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());

            int len;
            byte[] arr = new byte[2048];
            while ((len = bis.read(arr)) != -1) {
                outputStream.write(arr, 0, len);
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
