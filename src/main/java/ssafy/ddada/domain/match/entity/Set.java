package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Set extends BaseMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "set_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id")
    @Column(nullable = false)
    private Match match;

    @OneToOne
    @JoinColumn(name = "team_id")
    @Column(nullable = false)
    private Team winner;

    @Column(nullable = false)
    private Integer setNumber;

    @OneToMany(mappedBy = "set", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();

    public Set (Match match, Team winner, Integer setNumber) {
        this.match = match;
        this.winner = winner;
        this.setNumber = setNumber;
    }

}
