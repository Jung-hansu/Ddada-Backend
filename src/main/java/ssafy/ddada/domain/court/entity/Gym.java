package ssafy.ddada.domain.court.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.domain.member.gymadmin.entity.GymAdmin;

import java.util.List;
import java.util.ArrayList;

@Getter
@Entity
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gym extends BaseCourtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private GymAdmin gymAdmin;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String contactNumber;

    private String description;

    @Setter
    private String image;

    private String url;

    @Enumerated(EnumType.STRING)
    private Region region;

    @OneToMany(mappedBy = "gym", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Court> courts = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        if (image == null){
            image = "https://ddada-image.s3.ap-northeast-2.amazonaws.com/profileImg/default.jpg";
        }
    }
}
