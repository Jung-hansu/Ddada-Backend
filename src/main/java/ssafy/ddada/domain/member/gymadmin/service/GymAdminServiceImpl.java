package ssafy.ddada.domain.member.gymadmin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.common.exception.gym.GymAdminNotFoundException;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.domain.member.gymadmin.entity.GymAdmin;
import ssafy.ddada.domain.member.gymadmin.repository.GymAdminRepository;

import static ssafy.ddada.common.util.ParameterUtil.nullToZero;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymAdminServiceImpl implements GymAdminService{

    private final GymAdminRepository gymAdminRepository;

    @Override
    @Transactional
    public void settleAccount(String account) {
        log.info("settleAccount >>>> account: {}", account);
        Long gymAdminId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        GymAdmin gymAdmin = gymAdminRepository.findByIdWithInfos(gymAdminId)
                .orElseThrow(GymAdminNotFoundException::new);

        doSettleAccount(gymAdmin, account);
    }

    private void doSettleAccount(GymAdmin gymAdmin, String account) {
        Integer amount = nullToZero(gymAdmin.getCumulativeIncome());

        gymAdmin.setCumulativeIncome(0);
        gymAdminRepository.save(gymAdmin);
        log.info("계좌 {}에 {}원을 지급했습니다.", account, amount);
    }

}
