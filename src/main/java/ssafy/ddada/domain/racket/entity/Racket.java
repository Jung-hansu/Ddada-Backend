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

    private String name;

    private String manufacturer;

    private Integer price;

    private String balance;

    private String weight;

    private String shaft;

    private String material;

    private String color;

    private String image;

}
