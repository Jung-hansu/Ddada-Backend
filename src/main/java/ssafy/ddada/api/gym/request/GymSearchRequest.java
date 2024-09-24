package ssafy.ddada.api.gym.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ssafy.ddada.domain.gym.command.GymSearchCommand;
import ssafy.ddada.domain.gym.entity.Region;

import static ssafy.ddada.common.util.ParameterUtil.blankToNull;

@Schema(description = "체육관 검색 요청 DTO")
public record GymSearchRequest(
        @Schema(description = "검색 키워드", example = "문화체육관")
        String keyword,
        @Schema(description = "지역 목록", example = "[\"서울\", \"부산\"]")
        String regions,
        @Schema(description = "페이지 번호")
        int page,
        @Schema(description = "페이지 크기")
        int size
) {
        public GymSearchCommand toCommand() {
                return new GymSearchCommand(
                        blankToNull(keyword),
                        Region.toRegionSet(regions),
                        PageRequest.of(page, size, Sort.by("id").descending())
                );
        }
}
