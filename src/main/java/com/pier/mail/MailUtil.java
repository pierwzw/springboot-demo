package com.pier.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @author zhongweiwu
 * @date 2019/4/2 18:25
 */
@Service
public class MailUtil {

    @Autowired
    private JavaMailSender jms;

    @Value("${spring.mail.username}")
    private String username;

    public void sendMail(String toUser, String subject, String text) {

        subject = "感谢您的注册~ ";

        text = "感谢你的注册，你的默认密码是："+"<h3>"+text+"</h3>";

        try {
            MimeMessage mimeMessage = jms.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            // 设置发信人，发信人需要和spring.mail.username配置的一样否则报错
            message.setFrom(username);
            // 设置收信人
            message.setTo(toUser);
            // 设置主题
            message.setSubject(subject);
            // 第二个参数true表示使用HTML语言来编写邮件
            message.setText(text, true);
            //FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/image/picture.jpg"));
            //helper.addAttachment("图片.jpg", file);//添加带附件的邮件
            //helper.addInline("picture", file);//添加带静态资源的邮件
            jms.send(mimeMessage);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
}