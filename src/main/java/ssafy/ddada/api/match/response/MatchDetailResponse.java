package ssafy.ddada.api.match.response;

import ssafy.ddada.domain.match.entity.SearchedMatch;

// TODO: 내부 record로 json 형식 지정하고 노션에 기록하기
public record MatchDetailResponse(
        SearchedMatch match
) {
    public static MatchDetailResponse of(SearchedMatch match) {
        return new MatchDetailResponse(match);
    }
}
