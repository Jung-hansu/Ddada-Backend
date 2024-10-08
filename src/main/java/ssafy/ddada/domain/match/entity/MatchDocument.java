package ssafy.ddada.domain.match.entity;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MatchDocument {

    @Field(type = Keyword, index = false)
    private String matchDate;

    @Field(type = Keyword, index = false)
    private String matchTime;

    public static MatchDocument from(Match match) {
        return MatchDocument.builder()
                .matchDate(match.getMatchDate().toString())
                .matchTime(match.getMatchTime().toString())
                .build();
    }

}
