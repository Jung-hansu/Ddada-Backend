package ssafy.ddada.domain.court.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

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
    @Enumerated(EnumType.STRING)
    private Region gymRegion;

//    private List<Match> matches = new ArrayList<>();

}
