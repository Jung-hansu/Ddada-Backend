package ssafy.ddada.api.match.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ssafy.ddada.domain.gym.entity.Region;
import ssafy.ddada.domain.match.command.MatchSearchCommand;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.match.entity.MatchType;
import ssafy.ddada.domain.match.entity.RankType;

import static ssafy.ddada.common.util.ParameterUtil.blankToNull;

@Schema(description = "경기 검색 요청 DTO")
public record MatchSearchRequest(
        @Schema(description = "검색 키워드", example = "문화체육관")
        String keyword,

        @Schema(description = "랭크 타입", example = "친선")
        String rankType,

        @Schema(description = "경기 타입", example = "남성복식,혼합복식")
        String matchTypes,

        @Schema(description = "경기 상태", example = "CREATED,RESERVED,PLAYING")
        String statuses,

        @Schema(description = "경기 지역", example = "서울,경기,부산")
        String regions,

        @Schema(description = "페이지 번호")
        int page,

        @Schema(description = "페이지 크기")
        int size
) {
    public MatchSearchCommand toCommand(){
        return new MatchSearchCommand(
                blankToNull(keyword),
                RankType.toRankType(rankType),
                MatchType.toMatchTypeSet(matchTypes),
                MatchStatus.toMatchStatusSet(statuses),
                Region.toRegionSet(regions),
                PageRequest.of(page, size, Sort.by("id").descending())
        );
    }
}
