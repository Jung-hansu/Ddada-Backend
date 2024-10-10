package ssafy.ddada.domain.member.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.member.manager.response.ManagerIdResponse;
import ssafy.ddada.common.exception.manager.ManagerNotFoundException;
import ssafy.ddada.api.member.manager.response.ManagerSignupResponse;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.common.util.JwtProcessor;
import ssafy.ddada.domain.member.manager.command.ManagerSignupCommand;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.api.member.manager.response.ManagerDetailResponse;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProcessor jwtProcessor;

    @Override
    public ManagerDetailResponse getManager() {
        log.info("[ManagerService] 매니저 정보 조회");
        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(ManagerNotFoundException::new);
        return ManagerDetailResponse.from(manager);
    }

    @Override
    @Transactional
    public ManagerSignupResponse signupManager(ManagerSignupCommand command) {
        log.info("[ManagerService] 매니저 회원가입");
        Manager manager = Manager.builder()
                .email(command.email())
                .nickname(command.nickname())
                .password(passwordEncoder.encode(command.password()))
                .number(command.number())
                .description(command.description())
                .build();
        Manager signupManager = managerRepository.save(manager);

        String accessToken = jwtProcessor.generateAccessToken(signupManager);
        String refreshToken = jwtProcessor.generateRefreshToken(signupManager);
        jwtProcessor.saveRefreshToken(accessToken, refreshToken);

        return ManagerSignupResponse.of(accessToken, refreshToken);
    }

    @Override
    public ManagerIdResponse getManagerId() {
        log.info("[ManagerService] 매니저 ID 조회");
        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        return ManagerIdResponse.of(managerId);
    }
}
