package ssafy.ddada.domain.court.service;

import ssafy.ddada.api.gym.response.GymDetailResponse;
import ssafy.ddada.api.gym.response.GymMatchesResponse;

import java.time.LocalDate;

public interface GymService {

    GymDetailResponse getGymInfo(Long gymAdminId);
    GymMatchesResponse getGymMatches(Long gymAdminId, LocalDate date);

}
