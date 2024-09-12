package ssafy.ddada.domain.court.entity;

import java.util.HashSet;
import java.util.Set;

public enum Facility {
    PARKING,
    SHOWER,
    TOILET,
    WIFI;

    public static Long bitMask(Set<Facility> facilities) {
        long bitMask = 0;
        for (Facility f : facilities) {
            bitMask |= 1 << f.ordinal();
        }
        return bitMask;
    }

    public static Set<Facility> bitMaskToSet(Long bitMask) {
        Set<Facility> facilities = new HashSet<>();
        for (Facility f : Facility.values()) {
            if ((bitMask & (1 << f.ordinal())) != 0) {
                facilities.add(f);
            }
        }
        return facilities;
    }
}
