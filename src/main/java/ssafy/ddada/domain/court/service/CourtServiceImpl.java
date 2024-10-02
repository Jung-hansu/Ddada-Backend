package ssafy.ddada.domain.court.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.common.exception.gym.CourtNotFoundException;
import ssafy.ddada.common.util.S3Util;
import ssafy.ddada.domain.court.command.CourtSearchCommand;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.CourtDocument;
import ssafy.ddada.domain.court.entity.Gym;
import ssafy.ddada.domain.court.entity.Region;
import ssafy.ddada.domain.court.repository.CourtElasticsearchRepository;
import ssafy.ddada.domain.court.repository.CourtRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ssafy.ddada.common.util.ParameterUtil.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final CourtElasticsearchRepository courtElasticsearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final S3Util s3Util;

    @Override
    public CourtDetailResponse getCourtById(Long courtId) {
        Court court = courtRepository.findCourtWithMatchesById(courtId)
                .orElseThrow(CourtNotFoundException::new);

        String presignedUrl = s3Util.getPresignedUrlFromS3(court.getGym().getImage());  // S3Util의 메서드 사용
        court.getGym().setImage(presignedUrl);
        return CourtDetailResponse.from(court);
    }

    @Override
    public Page<CourtSimpleResponse> getElasticFilteredCourts(CourtSearchCommand command) {
        String keyword = nullToBlank(command.keyword());
        Set<Region> regions = nullToEmptySet(command.regions());

        Criteria criteria = new Criteria();
        if (!isEmptyString(keyword)) {
            criteria = criteria.or("gymName").matches(keyword)
                    .or("gymAddress").matches(keyword);
        }
        if (!isEmptySet(regions)) {
            criteria = criteria.and("region").in(regions);
        }

        CriteriaQuery query = new CriteriaQuery(criteria);
        List<CourtSimpleResponse> courts = elasticsearchOperations.search(query, CourtDocument.class)
                .map(searchHit -> {
                    CourtDocument courtDocument = searchHit.getContent();
                    String image = Objects.requireNonNull(courtDocument.getGymImage());
                    String presignedUrl = s3Util.getPresignedUrlFromS3(image);
                    return CourtSimpleResponse.from(courtDocument, presignedUrl);
                })
                .toList();

        return new PageImpl<>(courts, command.pageable(), courts.size());
    }

    @Override
    public void indexAll() {
        List<Court> courts = courtRepository.findAll();
        for (Court court : courts) {
            indexCourt(court);
        }
    }

    private void indexCourt(Court court) {
        Gym gym = court.getGym();
        CourtDocument courtDocument = CourtDocument.builder()
                .id(String.valueOf(court.getId()))
                .courtId(court.getId())
                .gymName(gym != null ? gym.getName() : null)
                .gymAddress(gym != null ? gym.getAddress() : null)
                .gymDescription(gym != null ? gym.getDescription() : null)
                .gymContactNumber(gym != null ? gym.getContactNumber() : null)
                .gymImage(gym != null ? gym.getImage() : null)
                .gymUrl(gym != null ? gym.getUrl() : null)
                .gymRegion(gym != null ? gym.getRegion() : null)
                .build();

        courtElasticsearchRepository.save(courtDocument);
    }

}

