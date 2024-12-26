package com.sumui.common.utils.wechat;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sumui.common.model.constants.WXConstant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WechatUtil {

    public static JSONObject getSessionKeyOrOpenId(WXConstant wxConstant, String code) {
        log.info("wxConstant:{}", wxConstant);
        String sessionKeyOrOpenId = wxConstant.getUrl() + "&appid=" + wxConstant.getAppid() +
                "&secret=" + wxConstant.getSecret() +
                "&js_code=" + code;
        try (HttpResponse response = HttpUtil.createGet(sessionKeyOrOpenId).execute()) {
            String resp = response.body();
            log.info("resp:{}", resp);
            return JSONUtil.parseObj(resp);
        }
    }
}
