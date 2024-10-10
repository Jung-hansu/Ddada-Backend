package ssafy.ddada.domain.court.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.domain.gym.entity.Gym;
import ssafy.ddada.domain.match.entity.Match;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Court extends BaseCourtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @Column(nullable = false)
    private Integer courtNumber;

    @Builder.Default
    @OneToMany(mappedBy = "court", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();

}
