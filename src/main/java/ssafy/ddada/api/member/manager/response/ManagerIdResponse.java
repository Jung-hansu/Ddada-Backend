package ssafy.ddada.api.member.manager.response;

public record ManagerIdResponse(
        Long id
){
    public static ManagerIdResponse of(Long id) {
        return new ManagerIdResponse(
                id
        );
    }
}
