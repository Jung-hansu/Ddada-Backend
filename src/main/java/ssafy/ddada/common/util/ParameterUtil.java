package ssafy.ddada.common.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ParameterUtil {

    public static boolean isEmptyString(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String blankToNull(String str) {
        return str == null || str.trim().isEmpty() ? null : str;
    }

    public static boolean nullToFalse(Boolean bool){
        return bool != null && bool;
    }

}
