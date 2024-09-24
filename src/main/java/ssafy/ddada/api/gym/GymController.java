package ssafy.ddada.api.gym;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.gym.request.GymCreateRequest;
import ssafy.ddada.api.gym.request.GymSearchRequest;
import ssafy.ddada.api.gym.response.GymDetailResponse;
import ssafy.ddada.api.gym.response.GymSimpleResponse;
import ssafy.ddada.domain.gym.service.GymService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/gyms")
@Tag(name = "Gym", description = "체육관관리")
public class GymController {

    private final GymService gymService;

    @Operation(summary = "체육관 검색 결과 조회", description = "페이징된 체육관 검색 결과를 조회하는 API입니다.")
    @GetMapping("/search")
    public CommonResponse<Page<GymSimpleResponse>> getGymsByKeyword(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String regions,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        GymSearchRequest request = new GymSearchRequest(keyword, regions, page, size);
        log.info("체육관 검색 결과 조회 >>>> 검색어: {}, 검색 지역: {}, 페이지 번호: {}, 페이지 크기: {}", request.keyword(), request.regions(), request.page(), request.size());

        Page<GymSimpleResponse> response = gymService.getFilteredGyms(request.toCommand());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "체육관 세부 조회", description = "체육관 세부 정보를 조회하는 api입니다.")
    @GetMapping("/{gym_id}")
    public CommonResponse<GymDetailResponse> getGymById(@PathVariable("gym_id") Long gymId){
        log.info("체육관 세부 조회 >>>> 체육관 ID: {}", gymId);

        GymDetailResponse response = gymService.getGymById(gymId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "배드민턴 코트 추가", description = "새로운 배드민턴 코트를 추가하는 API입니다.")
    @PostMapping(value = "", consumes = { "multipart/form-data" })
    public ResponseEntity<Void> createBadmintonGym(@ModelAttribute @Validated GymCreateRequest request) {
        gymService.createGym(request);
        return ResponseEntity.ok().build();
    }
}
