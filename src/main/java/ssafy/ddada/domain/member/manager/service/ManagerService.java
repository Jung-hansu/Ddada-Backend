package ssafy.ddada.domain.member.manager.service;

import ssafy.ddada.api.member.manager.response.ManagerDetailResponse;
import ssafy.ddada.api.member.manager.response.ManagerSignupResponse;
import ssafy.ddada.domain.member.manager.command.ManagerSignupCommand;

public interface ManagerService {

    ManagerDetailResponse getManagerById(Long managerId);
    ManagerSignupResponse signupManager(ManagerSignupCommand managerSignupCommand);
}
