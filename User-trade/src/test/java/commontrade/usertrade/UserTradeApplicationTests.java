package commontrade.usertrade;

import commontrade.usertrade.email.EmailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTradeApplicationTests {

    @Autowired
    private EmailService service;
    @Test
    void contextLoads() {
    }

    @Test
    void emailSendTest() throws MessagingException {
        service.sendCode("1993480461@qq.com");
        System.out.println("success");
    }

    @Test
    void emailCheckTest() throws MessagingException {
        boolean result = service.checkEmailCode("1993480461@qq.com", "460526");
        if (result) {
            System.out.println("success");
        } else  {
            System.out.println("fail");
        }
    }

}
