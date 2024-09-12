package ssafy.ddada.domain.court.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ddada.domain.match.entity.BaseMatchEntity;
import ssafy.ddada.domain.match.entity.Match;

import java.util.List;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Court extends BaseMatchEntity {

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

    private String image;

    @OneToMany(mappedBy = "court", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();

    // 편의시설 컬렉션을 관리하는 ElementCollection 설정
//    @ElementCollection(fetch = FetchType.EAGER)  // Court 엔티티를 조회할 때 편의시설 목록도 즉시 로딩(EAGER)
//    @Enumerated(EnumType.STRING)  // Enum 값을 저장할 때 문자열(String) 형태로 저장
//    @CollectionTable(name = "court_facilities", joinColumns = @JoinColumn(name = "court_id"))  // 별도의 테이블 court_facilities에서 관리, court_id를 외래키로 사용
//    @Column(name = "facility")  // 테이블에서 해당 컬렉션의 컬럼 이름을 "facility"로 지정
    private Long facilities;  // 편의시설을 Set 형태로 관리, 기본값은 빈 HashSet
}
