package ssafy.ddada.domain.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.domain.court.service.CourtService;
import ssafy.ddada.domain.racket.service.RacketService;

@Slf4j
//@Component
//@DependsOn({"gymInitializer", "racketInitializer"})
@RequiredArgsConstructor
public class IndexInitializer {

    private final CourtService courtService;
    private final RacketService racketService;

    @Transactional
    @PostConstruct
    public void index(){
        courtService.indexAll();
        racketService.indexAll();
    }

}
