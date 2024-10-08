package ssafy.ddada.api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import ssafy.ddada.domain.auth.command.SmsCommand;

@Schema(description = "SMS 요청 DTO")
public record SmsRequest (
        @NotEmpty(message = "휴대폰 번호를 입력해주세요")
        String phoneNum
){
    public SmsCommand toCommand(){
        return new SmsCommand(phoneNum);
    }
}
