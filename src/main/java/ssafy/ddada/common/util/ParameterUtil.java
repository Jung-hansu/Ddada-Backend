package ssafy.ddada.common.util;

import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ParameterUtil {
    public static boolean isEmptyString(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNullOrZero(Long num) {
        return num == null || num == 0;
    }

    public static boolean isEmptyCollection(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
