package ssafy.ddada.api.court.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.entity.Facility;

import java.util.Set;

@Schema(description = "배드민턴 코트 생성 요청 DTO")
public record CourtCreateRequest(
        @Schema(description = "코트 이름", example = "문화체육관 배드민턴장")
        String name,

        @Schema(description = "코트 주소", example = "서울시 강남구")
        String address,

        @Schema(description = "연락처", example = "02-123-4567")
        String contactNumber,

        @Schema(description = "코트 설명")
        String description,

        @Schema(description = "코트 이미지 URL")
        String imageUrl,

        @Schema(description = "코트 편의시설 목록", example = "[\"PARKING\", \"SHOWER\"]")
        Set<Facility> facilities
) {}
