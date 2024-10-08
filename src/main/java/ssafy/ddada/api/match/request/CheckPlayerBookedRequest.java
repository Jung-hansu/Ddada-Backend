package ssafy.ddada.api.match.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.command.CheckPlayerBookedCommand;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "선수의 경기 시간 중복 확인 요청 DTO")
public record CheckPlayerBookedRequest(
        @Schema(description = "경기 일자", example = "2024-09-06", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate date,

        @Schema(description = "경기 시간", example = "10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalTime time
){
    public CheckPlayerBookedCommand toCommand(){
        return new CheckPlayerBookedCommand(date, time);
    }
}
