package ssafy.ddada.api.match.request;

import ssafy.ddada.domain.match.command.MatchResultCommand;

// TODO: 구현하기
public record MatchResultRequest(

) {
    public MatchResultCommand toCommand(){
        return new MatchResultCommand();
    }
}
