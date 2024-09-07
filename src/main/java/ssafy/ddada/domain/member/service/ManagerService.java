package ssafy.ddada.domain.member.service;

import ssafy.ddada.domain.member.entity.SearchedManager;

public interface ManagerService {

    SearchedManager getManagerById(Long managerId);

}
