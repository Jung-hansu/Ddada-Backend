package ssafy.ddada.api.data.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "추천 라켓 응답 DTO")
public record RacketRecommendResponse(
        @Schema(description = "플레이어 타입 정보")
        PlayerType myType,

        @Schema(description = "추천 라켓 리스트")
        List<Racket> racket
) {
    @Schema(description = "플레이어 타입 정보")
    public record PlayerType(
            @Schema(description = "닉네임", example = "날카로운 공격형")
            String nickname,

            @Schema(description = "설명", example = "날카로운 공격형 라켓은 주로 공격적인 성향을 가진 플레이어들이 선호하는 라켓입니다.")
            String explanation
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Schema(description = "라켓 정보")
    public record Racket(
            @Schema(description = "라켓 ID", example = "533")
            int racketId,

            @Schema(description = "라켓 이름", example = "아디다스 부흐트 P7")
            String name,

            @Schema(description = "가격", example = "203000")
            int price,

            @Schema(description = "밸런스", example = "헤드헤비(공격형)")
            String balance,

            @Schema(description = "무게", example = "4U")
            String weight,

            @Schema(description = "샤프트", example = "견고(Stiff)")
            String shaft,

            @Schema(description = "재질", example = "그라파이트(카본)")
            String material,

            @Schema(description = "색상", example = "null")
            String color,

            @Schema(description = "제조사", example = "아디다스")
            String manufacturer,

            @Schema(description = "이미지 URL", example = "//img.danawa.com/prod_img/500000/399/434/img/6434399_1.jpg")
            String image,

            @Schema(description = "타입 설명", example = "검색 조건에 부합한 라켓 중 가장 저렴해요.😁")
            String type
    ) {}
}
