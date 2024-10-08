package ssafy.ddada.domain.court.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import static org.springframework.data.elasticsearch.annotations.FieldType.*;

@Getter
@Builder
@ToString
@Setting(replicas = 0)
@Document(indexName = "court")
@NoArgsConstructor
@AllArgsConstructor
public class CourtDocument {

    @Id
    @Field(type = Keyword)
    private String id;

    @Field(type = Long, index = false, docValues = false)
    private Long courtId;

    @Field(type = Text, analyzer = "nori")
    private String gymName;

    @Field(type = Text, analyzer = "nori")
    private String gymAddress;

    @Field(type = Keyword)
    private String gymRegion;

}
