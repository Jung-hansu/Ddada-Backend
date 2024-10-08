package ssafy.ddada.domain.gym.service;

import ssafy.ddada.api.gym.response.GymDetailResponse;
import ssafy.ddada.api.gym.response.GymMatchesHistoryResponse;
import ssafy.ddada.api.gym.response.GymMatchesResponse;

import java.time.LocalDate;

public interface GymService {

    GymDetailResponse getGymInfo();
    GymMatchesResponse getGymMatches(LocalDate date);
    GymMatchesHistoryResponse getGymMatchesHistory();

}
