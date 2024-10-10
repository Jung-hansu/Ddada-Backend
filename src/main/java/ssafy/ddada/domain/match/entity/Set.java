package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Set extends BaseMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "set_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    private Integer setWinnerTeamNumber;

    @Column(nullable = false)
    private Integer setNumber;

    private Integer team1Score;

    private Integer team2Score;

    @Builder.Default
    @OneToMany(mappedBy = "set", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();

}
