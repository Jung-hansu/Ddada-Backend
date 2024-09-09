package ssafy.ddada.domain.manager.service;

import ssafy.ddada.api.manager.response.ManagerDetailResponse;

public interface ManagerService {

    ManagerDetailResponse getManagerById(Long managerId);

}
