package com.sumui.config.satoken;


import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import org.apache.commons.codec.cli.Digest;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: SaToken 注解式鉴权  默认不加注解不进行检查（默认放行）
 * @author: Sunl
 * @date: 2023-12-21
 **/
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    /** 注册Sa-Token的注解拦截器，打开注解式鉴权功能 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login","/sumui/user/add");
    }

    public static void main(String[] args) {
//        System.err.println("===========摘要加密==============");
//        digestEncryption();
        System.err.println("===========对称加密==============");
        aes_encryption();
//        System.err.println("===========非对称加密==============");
//        asymmetricEncryption();
    }

    /** 非对称加密 */
    private static void asymmetricEncryption() {
        // 定义私钥和公钥
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAO+wmt01pwm9lHMdq7A8gkEigk0XKMfjv+4IjAFhWCSiTeP7dtlnceFJbkWxvbc7Qo3fCOpwmfcskwUc3VSgyiJkNJDs9ivPbvlt8IU2bZ+PBDxYxSCJFrgouVOpAr8ar/b6gNuYTi1vt3FkGtSjACFb002/68RKUTye8/tdcVilAgMBAAECgYA1COmrSqTUJeuD8Su9ChZ0HROhxR8T45PjMmbwIz7ilDsR1+E7R4VOKPZKW4Kz2VvnklMhtJqMs4MwXWunvxAaUFzQTTg2Fu/WU8Y9ha14OaWZABfChMZlpkmpJW9arKmI22ZuxCEsFGxghTiJQ3tK8npj5IZq5vk+6mFHQ6aJAQJBAPghz91Dpuj+0bOUfOUmzi22obWCBncAD/0CqCLnJlpfOoa9bOcXSusGuSPuKy5KiGyblHMgKI6bq7gcM2DWrGUCQQD3SkOcmia2s/6i7DUEzMKaB0bkkX4Ela/xrfV+A3GzTPv9bIBamu0VIHznuiZbeNeyw7sVo4/GTItq/zn2QJdBAkEA8xHsVoyXTVeShaDIWJKTFyT5dJ1TR++/udKIcuiNIap34tZdgGPI+EM1yoTduBM7YWlnGwA9urW0mj7F9e9WIQJAFjxqSfmeg40512KP/ed/lCQVXtYqU7U2BfBTg8pBfhLtEcOg4wTNTroGITwe2NjL5HovJ2n2sqkNXEio6Ji0QQJAFLW1Kt80qypMqot+mHhS+0KfdOpaKeMWMSR4Ij5VfE63WzETEeWAMQESxzhavN1WOTb3/p6icgcVbgPQBaWhGg==";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDvsJrdNacJvZRzHauwPIJBIoJNFyjH47/uCIwBYVgkok3j+3bZZ3HhSW5Fsb23O0KN3wjqcJn3LJMFHN1UoMoiZDSQ7PYrz275bfCFNm2fjwQ8WMUgiRa4KLlTqQK/Gq/2+oDbmE4tb7dxZBrUowAhW9NNv+vESlE8nvP7XXFYpQIDAQAB";

        // 文本
        String text = "Sa-Token 一个轻量级java权限认证框架";

        // 使用公钥加密
        String ciphertext = SaSecureUtil.rsaEncryptByPublic(publicKey, text);
        System.out.println("公钥加密后：" + ciphertext);

        // 使用私钥解密
        String text2 = SaSecureUtil.rsaDecryptByPrivate(privateKey, ciphertext);
        System.out.println("私钥解密后：" + text2);

    }

    /** 对称加密 */
    private static void aes_encryption() {
        // 定义秘钥和明文
        String key = "123456";
        String text = "Sa-Token 一个轻量级java权限认证框架";

        // 加密
        String ciphertext = SaSecureUtil.aesEncrypt(key, text);
        String othertext = SaSecureUtil.aesEncrypt("sumui", text);
        System.out.println("AES加密后：" + ciphertext);
        System.out.println("AES加密后----sumui：" + othertext);

        // 解密
        String text2 = SaSecureUtil.aesDecrypt(key, ciphertext);
        String text3 = SaSecureUtil.aesDecrypt("sumui", othertext);
        System.out.println("AES解密后：" + text2);
        System.out.println("AES解密后--sumui：" + text3);

    }

    /** 摘要加密 */
    private static void digestEncryption() {
        // md5加密 
        String md5 = SaSecureUtil.md5("123456");
        System.err.println("md5--" + md5);

        // sha1加密 
        String sha1 = SaSecureUtil.sha1("123456");
        System.err.println("sha1--" + sha1);

        // sha256加密 
        String sha256 = SaSecureUtil.sha256("123456");
        System.err.println("sha256--" + sha256);
    }
}


