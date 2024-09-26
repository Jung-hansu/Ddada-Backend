package ssafy.ddada.domain.racket.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Racket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "racket_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String weight;

    @Column(nullable = false)
    private String material;

    private String image;

}
