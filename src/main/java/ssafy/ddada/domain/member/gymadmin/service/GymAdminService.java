package ssafy.ddada.domain.member.gymadmin.service;

import ssafy.ddada.domain.court.command.SettleAccountCommand;

public interface GymAdminService {
    void settleAccount(SettleAccountCommand command);
}