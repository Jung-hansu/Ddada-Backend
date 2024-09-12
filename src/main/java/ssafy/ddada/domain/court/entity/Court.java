package ssafy.ddada.domain.court.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import ssafy.ddada.domain.match.entity.Match;

import java.util.List;
import java.util.ArrayList;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Court extends BaseCourtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String contactNumber;

    private String description;

    @Setter
    private String image;

    private String url;

    @OneToMany(mappedBy = "court", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();

    // 시설 편의시설 비트마스킹
    private long facilities;

    public Court(String name, String address, String contactNumber, String description, String image, String url, long facilities) {
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.description = description;
        this.image = image;
        this.url = url;
        this.facilities = facilities;
    }

    public static Court createCourt(String name, String address, String contactNumber, String description, String image, String url, long facilities) {
        return new Court(name, address, contactNumber, description, image, url, facilities);
    }
}
