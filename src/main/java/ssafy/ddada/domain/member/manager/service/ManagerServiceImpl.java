package ssafy.ddada.domain.member.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.member.manager.response.ManagerSignupResponse;
import ssafy.ddada.common.exception.ManagerNotFoundException;
import ssafy.ddada.config.auth.JwtProcessor;
import ssafy.ddada.domain.member.manager.command.ManagerSignupCommand;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.api.member.manager.response.ManagerDetailResponse;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProcessor jwtProcessor;

    @Override
    public ManagerDetailResponse getManagerById(Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(ManagerNotFoundException::new);
        return ManagerDetailResponse.from(manager);
    }

    @Override
    public ManagerSignupResponse signupManager(ManagerSignupCommand managerSignupCommand) {
        Manager manager = new Manager();

        manager.signupManager(
                managerSignupCommand.email(),
                managerSignupCommand.nickname(),
                passwordEncoder.encode(managerSignupCommand.password()),
                null,
                managerSignupCommand.number(),
                managerSignupCommand.description()
        );

        Manager signupManager = managerRepository.save(manager);

        String accessToken = jwtProcessor.generateAccessToken(signupManager);
        String refreshToken = jwtProcessor.generateRefreshToken(signupManager);
        jwtProcessor.saveRefreshToken(accessToken, refreshToken);

        return ManagerSignupResponse.of(accessToken, refreshToken);
    }

}
