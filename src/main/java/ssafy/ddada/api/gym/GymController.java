package ssafy.ddada.api.gym;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.gym.request.SettleAccountRequest;
import ssafy.ddada.api.gym.response.GymDetailResponse;
import ssafy.ddada.api.gym.response.GymMatchesHistoryResponse;
import ssafy.ddada.api.gym.response.GymMatchesResponse;
import ssafy.ddada.common.exception.gym.GymAdminNotFoundException;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.domain.court.service.GymService;
import ssafy.ddada.domain.member.gymadmin.service.GymAdminService;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/gym")
@PreAuthorize("hasRole('ROLE_GYM_ADMIN')")
@Tag(name = "Gym", description = "체육관 관리")
public class GymController {

    private final GymService gymService;
    private final GymAdminService gymAdminService;

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
        GymMatchesResponse response = gymService.getGymMatches(gymAdminId, date);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "체육관 최근 일주일간 경기 수 조회", description = "체육관의 최근 일주일간 경기 수를 조회하는 API입니다.")
    @GetMapping("/matches/history")
    public CommonResponse<GymMatchesHistoryResponse> getCurrentGymMatchesHistory(){
        Long gymAdminId = SecurityUtil.getLoginMemberId().orElseThrow(GymAdminNotFoundException::new);
        log.info("getCurrentGymMatchesHistory >>>> gymAdminId: {}", gymAdminId);
        GymMatchesHistoryResponse response = gymService.getGymMatchesHistory(gymAdminId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "체육관 수익 인출", description = "체육관 수익을 인출하는 API입니다.")
    @PatchMapping("/withdraw")
    public CommonResponse<?> settleAccount(@RequestBody SettleAccountRequest request){
        Long gymAdminId = SecurityUtil.getLoginMemberId().orElseThrow(GymAdminNotFoundException::new);
        log.info("settleAccount >>>> gymAdminId: {}, account: {}", gymAdminId, request);
        gymAdminService.settleAccount(request.toCommand());
        return CommonResponse.ok("정상 송금 되었습니다.", null);
    }


}
