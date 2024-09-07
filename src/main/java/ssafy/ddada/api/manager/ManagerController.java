package ssafy.ddada.api.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.manager.response.ManagerDetailResponse;
import ssafy.ddada.domain.member.entity.Manager;
import ssafy.ddada.domain.member.entity.SearchedManager;
import ssafy.ddada.domain.member.service.ManagerService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/{managerId}")
    public ManagerDetailResponse getManager(@PathVariable("managerId") Long managerId){
        SearchedManager manager = managerService.getManagerById(managerId);
        return ManagerDetailResponse.of(manager);
    }

}
