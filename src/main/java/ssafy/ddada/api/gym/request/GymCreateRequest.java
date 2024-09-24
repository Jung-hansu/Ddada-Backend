package ssafy.ddada.api.gym.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.domain.gym.entity.Region;

@Schema(description = "배드민턴 코트 생성 요청 DTO")
public record GymCreateRequest(
        @Schema(description = "코트 이름", example = "문화체육관 배드민턴장")
        String name,

        @Schema(description = "코트 주소", example = "서울시 강남구")
        String address,

        @Schema(description = "연락처", example = "02-123-4567")
        String contactNumber,

        @Schema(description = "코트 설명")
        String description,

        @Schema(description = "코트 이미지")
        MultipartFile image,

        @Schema(description = "코트 홈페이지 URL")
        String url,

        @Schema(description = "코트 지역", example = "SEOUL")
        Region region
) {}
