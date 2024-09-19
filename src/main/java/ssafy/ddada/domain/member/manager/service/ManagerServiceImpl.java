package ssafy.ddada.domain.member.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.ddada.common.exception.Exception.Manager.ManagerNotFoundException;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.api.member.manager.response.ManagerDetailResponse;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;

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
