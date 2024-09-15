package ssafy.ddada.common.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ParameterUtil {

    public static boolean isEmptyString(String str) {
        return str == null || str.trim().isEmpty();
    }

}
