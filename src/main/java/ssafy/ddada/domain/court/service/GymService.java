package ssafy.ddada.domain.court.service;

import ssafy.ddada.api.gym.response.GymDetailResponse;

public interface GymService {

    GymDetailResponse getGymInfo(Long gymAdminId);


}
