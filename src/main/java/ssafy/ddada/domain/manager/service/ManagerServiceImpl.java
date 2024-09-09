package ssafy.ddada.domain.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.ddada.common.exception.ManagerNotFoundException;
import ssafy.ddada.domain.manager.entity.Manager;
import ssafy.ddada.api.manager.response.ManagerDetailResponse;
import ssafy.ddada.domain.manager.repository.ManagerRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    @Override
    public ManagerDetailResponse getManagerById(Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(ManagerNotFoundException::new);
        return ManagerDetailResponse.from(manager);
    }

}
