package com.oddfar.campus.business.api;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import com.oddfar.campus.business.entity.ILog;
import com.oddfar.campus.business.entity.IUser;
import com.oddfar.campus.common.utils.StringUtils;
import com.oddfar.campus.framework.manager.AsyncManager;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * @author zhiyuan
 */
public class PushPlusApi {


    public static void sendNotice(IUser iUser, ILog operLog) {
        String token = iUser.getPushPlusToken();
        if (StringUtils.isEmpty(token)) {
            return;
        }
        String title, content;
        if (operLog.getStatus() == 0) {
            //预约成功
            title = iUser.getRemark() + "-i茅台执行成功";
            content = iUser.getMobile() + System.lineSeparator() + operLog.getLogContent();
            AsyncManager.me().execute(sendNotice(token, title, content, "txt"));
        } else {
            //预约失败
            title = iUser.getRemark() + "-i茅台执行失败";
            content = iUser.getMobile() + System.lineSeparator() + operLog.getLogContent();
            AsyncManager.me().execute(sendNotice(token, title, content, "txt"));
        }


    }

    /**
     * push推送
     *
     * @param token    token
     * @param title    消息标题
     * @param content  具体消息内容
     * @param template 发送消息模板
     */
    public static TimerTask sendNotice(String token, String title, String content, String template) {
        return new TimerTask() {
            @Override
            public void run() {
                // String url = "http://www.pushplus.plus/send";
                // Map<String, Object> map = new HashMap<>();
                // map.put("token", token);
                // map.put("title", title);
                // map.put("content", content);
                // if (StringUtils.isEmpty(template)) {
                //     map.put("template", "html");
                // }
                // HttpUtil.post(url, map);

                String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=4c88d2bd-ba09-44b7-b380-4532b8c8c4e7";
                Map<String, Object> message = new HashMap<>();
                Map<String, String> text = new HashMap<>();
                text.put("content", content);
                message.put("msgtype", "text");
                message.put("text", text);

                String jsonMessage = JSONUtil.toJsonStr(message);
                HttpUtil.createPost(url)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage)
                    .execute();
                
            }
        };
    }

}
