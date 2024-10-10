package ssafy.ddada.domain.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.CourtDocument;
import ssafy.ddada.domain.court.repository.CourtElasticsearchRepository;
import ssafy.ddada.domain.court.repository.CourtRepository;
import ssafy.ddada.domain.gym.entity.Gym;
import ssafy.ddada.domain.racket.entity.Racket;
import ssafy.ddada.domain.racket.entity.RacketDocument;
import ssafy.ddada.domain.racket.repository.RacketElasticsearchRepository;
import ssafy.ddada.domain.racket.repository.RacketRepository;
import ssafy.ddada.domain.racket.service.RacketService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
//@Component
//@DependsOn({"gymInitializer", "racketInitializer"})
@RequiredArgsConstructor
public class IndexInitializer {

    private final CourtRepository courtRepository;
    private final RacketRepository racketRepository;
    private final CourtElasticsearchRepository courtElasticsearchRepository;
    private final RacketElasticsearchRepository racketElasticsearchRepository;

    @PostConstruct
    public void index(){
        log.info("[IndexInitializer] DB에서 ES로 인덱싱");
        indexAllCourt();
        indexAllRacket();
    }

    private void indexAllCourt() {
        log.info("[CourtService] 코트 인덱싱");
        final int batchSize = 10;
        List<Court> courts = courtRepository.findAll();
        List<CourtDocument> courtDocuments = new ArrayList<>(batchSize);
        int size = courts.size(), cur = 0;

        for (Court court : courts) {
            courtDocuments.add(createCourtDocument(court));
            cur++;

            if (cur % batchSize == 0 || cur == size) {
                log.info("코트 인덱싱 진행도: {}%", Math.round(1000.0 * ++cur / size) / 10.0);
                courtElasticsearchRepository.saveAll(courtDocuments);
                courtDocuments.clear();
            }
        }
    }

    private void indexAllRacket() {
        final int batchSize = 100;
        List<Racket> rackets = racketRepository.findAll();
        List<RacketDocument> racketDocuments = new ArrayList<>(batchSize);
        int size = rackets.size(), cur = 0;

        for (Racket racket : rackets) {
            racketDocuments.add(createRacketDocument(racket));
            cur++;

            if (cur % batchSize == 0 || cur == size) {
                log.info("라켓 인덱싱 진행도: {}%", Math.round(1000.0 * cur / size) / 10.0);
                racketElasticsearchRepository.saveAll(racketDocuments);
                racketDocuments.clear();
            }
        }
    }

    private CourtDocument createCourtDocument(Court court) {
        Gym gym = court.getGym();
        return CourtDocument.builder()
                .id(String.valueOf(court.getId()))
                .courtId(court.getId())
                .gymName(gym != null ? gym.getName() : null)
                .gymAddress(gym != null ? gym.getAddress() : null)
                .gymRegion(gym != null ? gym.getRegion().getKorValue() : null)
                .build();
    }

    private RacketDocument createRacketDocument(Racket racket){
        return RacketDocument.builder()
                .racketId(racket.getId())
                .name(racket.getName())
                .manufacturer(racket.getManufacturer())
                .weight(racket.getWeight())
                .material(racket.getMaterial())
                .image(racket.getImage())
                .build();
    }

}
