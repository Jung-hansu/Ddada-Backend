package ssafy.ddada.domain.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import ssafy.ddada.domain.court.service.CourtService;
import ssafy.ddada.domain.racket.service.RacketService;

@Slf4j
//@Component
//@DependsOn({"gymInitializer", "racketInitializer"})
@RequiredArgsConstructor
public class IndexInitializer {

    private final CourtService courtService;
    private final RacketService racketService;

    @PostConstruct
    public void index(){
        log.info("[IndexInitializer] DB에서 ES로 인덱싱");
        courtService.indexAll();
        racketService.indexAll();
    }

}
