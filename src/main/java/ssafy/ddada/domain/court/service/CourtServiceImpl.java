package ssafy.ddada.domain.court.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.common.exception.gym.CourtNotFoundException;
import ssafy.ddada.common.util.S3Util;
import ssafy.ddada.domain.court.command.CourtSearchCommand;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.CourtDocument;
import ssafy.ddada.domain.court.repository.CourtRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ssafy.ddada.common.util.ParameterUtil.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final S3Util s3Util;

    @Override
    public CourtDetailResponse getCourtById(Long courtId) {
        log.info("[CourtService] 코트 조회 >>>> 코트 ID: {}", courtId);
        Court court = courtRepository.findCourtWithMatchesById(courtId)
                .orElseThrow(CourtNotFoundException::new);
        String presignedUrl = s3Util.getPresignedUrlFromS3(court.getGym().getImage());
        return CourtDetailResponse.from(court, presignedUrl);
    }

    @Override
    public Page<CourtSimpleResponse> getElasticFilteredCourts(CourtSearchCommand command) {
        log.info("[CourtService] ES 기반 코트 검색 >>>> 키워드: {}, 지역: {}", command.keyword(), command.regions());
        CriteriaQuery query = generateQuery(command.keyword(), command.regions(), command.pageable());
        SearchHits<CourtDocument> courtDocuments = elasticsearchOperations.search(query, CourtDocument.class);
        List<Long> courtIds = courtDocuments
                .map(searchHit -> searchHit.getContent().getCourtId())
                .toList();
        List<CourtSimpleResponse> courts = courtRepository.findCourtsByCourtIds(courtIds)
                .stream()
                .map(court -> {
                    String image = Objects.requireNonNull(court.getGym().getImage());
                    String presignedUrl = s3Util.getPresignedUrlFromS3(image);
                    return CourtSimpleResponse.from(court, presignedUrl);
                })
                .toList();

        return new PageImpl<>(courts, command.pageable(), courtDocuments.getTotalHits());
    }

    private CriteriaQuery generateQuery(String keyword, Set<String> regions, Pageable pageable){
        keyword = nullToBlank(keyword);
        regions = nullToEmptySet(regions);

        Criteria criteria = new Criteria();
        if (!isEmptyString(keyword)) {
            criteria = criteria
                    .or("gymName").matches(keyword)
                    .or("gymAddress").matches(keyword);
        }
        if (!isEmptySet(regions)) {
            log.info("regions: {}", regions);
            criteria = criteria.and("gymRegion").in(regions);
        }
        return new CriteriaQuery(criteria).setPageable(pageable);
    }

}

