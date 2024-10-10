package ssafy.ddada.domain.racket.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@ToString
@Builder
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
    private Integer price;

    private String balance;

    @Column(nullable = false)
    private String weight;

    private String shaft;

    @Column(nullable = false)
    private String material;

    private String color;

    @Column(nullable = false)
    private String image;

}
