package ssafy.ddada.domain.court.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시설 검색 결과 DTO")
public record SearchedCourt(
        Long id,
        String name,
        String address
) {
    public static SearchedCourt from(Court court){
        return new SearchedCourt(court.getId(), court.getName(), court.getAddress());
    }
}
