package ssafy.ddada.api.court;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ssafy.ddada.api.court.response.CourtSearchResponse;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.service.CourtService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/courts")
public class CourtController {

    private final CourtService courtService;

    @GetMapping
    public CourtSearchResponse getCourts(@RequestParam String keyword, @RequestParam int page, @RequestParam int size){
        return courtService.getCourts(keyword, page, size);
    }

}
