package ssafy.ddada.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.ddada.common.exception.ManagerNotFoundException;
import ssafy.ddada.domain.member.entity.Manager;
import ssafy.ddada.domain.member.entity.SearchedManager;
import ssafy.ddada.domain.member.repository.ManagerRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    @Override
    public SearchedManager getManagerById(Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(ManagerNotFoundException::new);
        return SearchedManager.from(manager);
    }
}
