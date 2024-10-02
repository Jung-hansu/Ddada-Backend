package ssafy.ddada.domain.racket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Setting;

import static org.springframework.data.elasticsearch.annotations.FieldType.*;

@Getter
@Builder
@Setting(replicas = 0)
@Document(indexName = "racket")
@NoArgsConstructor
@AllArgsConstructor
public class RacketDocument {

    @Id
    @Field(type = Keyword)
    private String id;

    @Field(type = Long, index = false, docValues = false)
    private Long racketId;

    @Field(type = Text, analyzer = "nori")
    private String name;

    @Field(type = Text, analyzer = "nori")
    private String manufacturer;

    @Field(type = Keyword, index = false)
    private String weight;

    @Field(type = Keyword, index = false)
    private String material;

    @Field(type = Text, index = false)
    private String image;

}
