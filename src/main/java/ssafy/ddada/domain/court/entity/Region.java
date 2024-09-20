package ssafy.ddada.domain.court.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ssafy.ddada.common.exception.court.InvalidRegionException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ssafy.ddada.common.util.ParameterUtil.isEmptyString;

@Getter
@RequiredArgsConstructor
public enum Region {

    SEOUL("서울"),
    GYEONGGI("경기"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    JEONNAM("전남"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    BUSAN("부산"),
    INCHEON("인천"),
    DAEGU("대구"),
    DAEJEON("대전"),
    GWANGJU("광주"),
    ULSAN("울산"),
    GANGWON("강원"),
    JEONBUK("전북"),
    JEJU("제주"),
    SEJONG("세종");

    private final String korValue;

    public static Region fromValue(String value) {
        return Stream.of(Region.values())
                .filter(region -> region.getKorValue().equals(value))
                .findFirst()
                .orElseThrow(InvalidRegionException::new);
    }

    public static Set<Region> toRegionSet(String regions){
        if (isEmptyString(regions)){
            return null;
        }
        return Stream.of(regions.split(","))
                .map(Region::fromValue)
                .collect(Collectors.toSet());
    }

}
