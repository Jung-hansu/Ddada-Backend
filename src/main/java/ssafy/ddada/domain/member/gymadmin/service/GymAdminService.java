package ssafy.ddada.domain.member.gymadmin.service;

import ssafy.ddada.domain.member.gymadmin.entity.GymAdmin;

public interface GymAdminService {

    GymAdmin settleAccount(Long gymAdminId, String account, Integer amount);

}