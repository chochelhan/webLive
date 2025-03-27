package com.riverflow.livegoapp.Service;

import lombok.AllArgsConstructor;
import handler.MailHandler;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class MailService {
    private JavaMailSender mailSender;

    public void mailSend(String to,String from, String subject, String content) {

            try {
                MailHandler mailHandler = new MailHandler(mailSender);

                // 받는 사람
                mailHandler.setTo(to);
                // 보내는 사람
                mailHandler.setFrom(from);
                // 제목
                mailHandler.setSubject(subject);
                // HTML Layout
                mailHandler.setText(content, true);
                // 첨부 파일
                //mailHandler.setAttach("newTest.txt", "static/originTest.txt");
                // 이미지 삽입
                //mailHandler.setInline("sample-img", "static/sample1.jpg");
                mailHandler.send();
            } catch (Exception e) {
                e.printStackTrace();
            }


    }
}
