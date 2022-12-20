package com.snow.storeapi.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;;
/**
 * 发送短信工具类
 */
@Component
public class SMSUtil {
    private final static Logger log = LoggerFactory.getLogger(SMSUtil.class);

    public static String secretId;

    public static String secretKey;

    public static String appId;

    public static String appKey;

    @Value("${CMS.AppID}")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Value("${CMS.AppKey}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Value("${TENCENTCLOUD.SECRET_ID}")
    public void setSecretId(String secretId) {
        SMSUtil.secretId = secretId;
    }

    @Value("${TENCENTCLOUD.SECRET_KEY}")
    public void setSecretKey(String secretKey) {
        SMSUtil.secretKey = secretKey;
    }

    /**
     * 根据模板，（批量）发送短信
     * @param templateId 模板ID
     * @param sign 签名
     * @param phoneNumber 收件人手机号
     * @param templateParamSet 参数
     * @return
     */
    public static JSONObject sendMessage(String templateId, String sign, String phoneNumber,String[] templateParamSet){
        log.info("发送短信参数，templateId={}，sign={}，phoneNumberSet={}，templateParamSet={}",templateId,sign,phoneNumber,templateParamSet.toString());
        JSONObject result = new JSONObject();
        try{
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "", clientProfile);

            SendSmsRequest req = new SendSmsRequest();

            //格式为+[国家或地区码][手机号]
            String[] phoneNumberSet = new String[]{"+86"+phoneNumber};
            req.setPhoneNumberSet(phoneNumberSet);


            req.setTemplateParamSet(templateParamSet);

            req.setTemplateID(templateId);
            req.setSmsSdkAppid(appId);
            req.setSign(sign);

            SendSmsResponse resp = client.SendSms(req);
            log.info("请求结果={}",SendSmsResponse.toJsonString(resp));
            JSONObject res = JSONObject.parseObject(SendSmsResponse.toJsonString(resp));
            JSONArray SendStatusSet = res.getJSONArray("SendStatusSet");
            StringBuilder sb = new StringBuilder();
            SendStatusSet.stream().forEach(o -> {
                JSONObject object = (JSONObject)o;
                if(!"Ok".equals(object.getString("Code"))){
                    sb.append("手机号码:"+object.getString("PhoneNumber")
                            +"发送失败，Code:"+object.getString("Code")
                            +"，Message:"+object.getString("Message")+"；");
                }
            });

            if (sb == null || sb.length() <= 0){
                result.put("code", 0);
                result.put("msg", "success");
            }else {
                result.put("code", 1);
                result.put("msg", sb.toString());
            }
        } catch (TencentCloudSDKException e) {
            result.put("code",1);
            result.put("msg",e.getMessage());
        }
        return result;
    }

}
