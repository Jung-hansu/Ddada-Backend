package ssafy.ddada.common.util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Component;
import ssafy.ddada.common.properties.CoolSmsProperties;

@Component
public class SmsCertificationUtil {

    private final CoolSmsProperties coolSmsProperties; // CoolSmsProperties를 주입받음
    private DefaultMessageService messageService; // 메시지 서비스를 위한 객체

    public SmsCertificationUtil(CoolSmsProperties coolSmsProperties) {
        this.coolSmsProperties = coolSmsProperties; // 생성자를 통해 coolSmsProperties를 주입받음
    }

    @PostConstruct // 의존성 주입이 완료된 후 초기화를 수행하는 메서드
    public void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(
                coolSmsProperties.apiKey(),
                coolSmsProperties.apiSecret(),
                "https://api.coolsms.co.kr"
        ); // 메시지 서비스 초기화
    }

    public void sendSMS(String to, String certificationCode){
        Message message = new Message(); // 새 메시지 객체 생성
        message.setFrom(coolSmsProperties.fromNumber()); // 발신자 번호 설정
        message.setTo(to); // 수신자 번호 설정
        message.setText("본인확인 인증번호는 " + certificationCode + "입니다."); // 메시지 내용 설정

        this.messageService.sendOne(new SingleMessageSendingRequest(message)); // 메시지 발송 요청
    }

}
