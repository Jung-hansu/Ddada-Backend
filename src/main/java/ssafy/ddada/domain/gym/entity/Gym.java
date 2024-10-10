package ssafy.ddada.domain.gym.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.common.constant.global.S3_IMAGE;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.member.gymadmin.entity.GymAdmin;

import java.util.List;
import java.util.ArrayList;

@Getter
@Entity
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gym extends BaseGymEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    @Builder.Default
    @OneToMany(mappedBy = "gym", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Court> courts = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        if (image == null){
            image = S3_IMAGE.DEFAULT_URL;
        }
    }
}
