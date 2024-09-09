package ssafy.ddada.domain.member.manager.service;

import ssafy.ddada.api.manager.response.ManagerDetailResponse;

public interface ManagerService {

    ManagerDetailResponse getManagerById(Long managerId);

}
