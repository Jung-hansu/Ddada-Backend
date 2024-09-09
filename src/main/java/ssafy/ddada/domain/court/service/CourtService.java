package ssafy.ddada.domain.court.service;

import org.springframework.data.domain.Page;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;

public interface CourtService {

    Page<CourtSimpleResponse> getCourtsByKeyword(String keyword, int page, int size);
    CourtDetailResponse getCourtById(Long courtId);

}
