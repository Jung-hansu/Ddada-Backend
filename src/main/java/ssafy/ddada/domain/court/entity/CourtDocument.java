package ssafy.ddada.domain.court.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import ssafy.ddada.domain.match.entity.MatchDocument;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.*;

@Getter
@Builder
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

    @Field(type = Text, index = false)
    private String gymDescription;

    @Field(type = Keyword, index = false)
    private String gymContactNumber;

    @Setter
    @Field(type = Keyword, index = false)
    private String gymImage;

    @Field(type = Keyword, index = false)
    private String gymUrl;

    @Field(type = Keyword)
    private String gymRegion;

    @Field(type = Nested, index = false)
    private List<MatchDocument> matches = new ArrayList<>();

}
