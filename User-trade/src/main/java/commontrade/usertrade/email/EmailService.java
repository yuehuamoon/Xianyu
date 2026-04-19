package commontrade.usertrade.email;


import cn.hutool.core.util.RandomUtil;
import commontrade.commonyh.pojo.dto.EmailCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class EmailService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    private final JavaMailSender javaMailSender;
    private final String FROM = "yuehua@94746424.xyz";

    // 构造器注入（Spring3推荐）
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    // 发送验证码
    public void sendCode(String to) throws MessagingException {
        String code = RandomUtil.randomNumbers(6);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 存储进redis中
        stringRedisTemplate.opsForValue().set(
                "email:code:"+ to,
                code,
                5,
                TimeUnit.MINUTES
        );

        helper.setFrom(FROM);
        helper.setTo(to);
        helper.setSubject("【账号验证】您的验证码");
        helper.setText("您的验证码是：" + code + "，5分钟内有效", false);
        System.out.println(code+to);
        javaMailSender.send(message);
    }

    public boolean checkEmailCode(EmailCode emailCode) throws MessagingException {
        String key = stringRedisTemplate.opsForValue().get("email:code:"+ emailCode.getEmail());

        if (key == null) {
            return false;
        }
        if (key.equals(emailCode.getCode())) {
            stringRedisTemplate.delete("email:code:"+ emailCode.getEmail());
            return true;
        }
        return false;

    }
}