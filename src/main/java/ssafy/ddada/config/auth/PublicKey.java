package ssafy.ddada.config.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PublicKey {
    private String kid; // public key id
    private String kty; // public key type : RSA로 고정
    private String alg; // algorithm : 암호화 알고리즘
    private String use; // public key use : 공개키 용도; sig(서명)으로 고정
    private String n; // modulus : 공개키 모듈
    private String e; // exponent : 공개키 지수
}
