package ssafy.ddada.domain.court.service;

import ssafy.ddada.api.court.response.CourtSearchResponse;

public interface CourtService {

    CourtSearchResponse getCourts(String keyword, int page, int size);

}
