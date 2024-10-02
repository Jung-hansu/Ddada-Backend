package ssafy.ddada.domain.court.service;

import org.springframework.data.domain.Page;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.domain.court.command.CourtSearchCommand;

public interface CourtService {

    Page<CourtSimpleResponse> getFilteredCourts(CourtSearchCommand command);
    CourtDetailResponse getCourtById(Long courtId);

    // Elasticsearch
    Page<CourtSimpleResponse> getElasticFilteredCourts(CourtSearchCommand command);
    void indexAllCourts();

}
