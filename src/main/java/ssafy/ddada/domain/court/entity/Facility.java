package ssafy.ddada.domain.court.entity;

import ssafy.ddada.common.exception.InvalidFacilityException;
import ssafy.ddada.common.util.StringUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Facility {
    PARKING,
    SHOWER,
    TOILET,
    WIFI;

    public static Facility of(String name) {
        return Arrays.stream(Facility.values())
                .filter(f -> f.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(InvalidFacilityException::new);
    }

    public static long toBits(String facilities) {
        long bitMask = 0;

        if (!StringUtil.isEmpty(facilities) && !facilities.isEmpty()) {
            for (String f : facilities.split(",")) {
                bitMask |= 1L << Facility.of(f).ordinal();
            }
        }
        return bitMask;
    }

    public static long setToBits(Set<Facility> facilities) {
        long bitMask = 0;

        if (facilities != null && !facilities.isEmpty()) {
            for (Facility f : facilities) {
                bitMask |= 1 << f.ordinal();
            }
        }
        return bitMask;
    }

    public static Set<Facility> bitsToSet(long bitMask) {
        Set<Facility> facilities = new HashSet<>();

        for (Facility f : Facility.values()) {
            if ((bitMask & (1 << f.ordinal())) != 0) {
                facilities.add(f);
            }
        }
        return facilities;
    }
}
