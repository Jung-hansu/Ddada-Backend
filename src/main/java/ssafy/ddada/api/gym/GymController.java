package ssafy.ddada.api.gym;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.gym.response.GymDetailResponse;
import ssafy.ddada.api.gym.response.GymMatchesResponse;
import ssafy.ddada.common.exception.gym.GymAdminNotFoundException;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.domain.court.service.GymService;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/gym")
@PreAuthorize("hasRole('ROLE_GYM_ADMIN')")
@Tag(name = "Gym", description = "체육관 관리")
public class GymController {

    private final GymService gymService;

    @Operation(summary = "체육관 조회", description = "체육관 정보를 조회하는 API입니다.")
    @GetMapping
    public CommonResponse<GymDetailResponse> getGymInfo(){
        Long gymAdminId = SecurityUtil.getLoginMemberId().orElseThrow(GymAdminNotFoundException::new);
        log.info("getGymInfo >>>> gymAdminId: {}", gymAdminId);
        GymDetailResponse response = gymService.getGymInfo(gymAdminId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "체육관 경기 현황 조회", description = "체육관에 예약된 경기 현황을 조회하는 API입니다.")
    @GetMapping("/matches")
    public CommonResponse<GymMatchesResponse> getGymMatches(@RequestParam LocalDate date){
        Long gymAdminId = SecurityUtil.getLoginMemberId().orElseThrow(GymAdminNotFoundException::new);
        log.info("getGymMatches >>>> gymAdminId: {}, date: {}", gymAdminId, date);

        return CommonResponse.ok();
    }

    @Operation(summary = "체육관 수익 인출", description = "체육관 수익을 인출하는 API입니다.")
    @PatchMapping("/withdraw")
    public CommonResponse<?> settleAccount(){
        Long gymAdminId = SecurityUtil.getLoginMemberId().orElseThrow(GymAdminNotFoundException::new);
        log.info("settleAccount >>>> gymAdminId: {}", gymAdminId);

        return CommonResponse.ok();
    }


}
