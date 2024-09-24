package ssafy.ddada.domain.gym.service;

import org.springframework.data.domain.Page;
import ssafy.ddada.api.gym.request.GymCreateRequest;
import ssafy.ddada.api.gym.response.GymDetailResponse;
import ssafy.ddada.api.gym.response.GymSimpleResponse;
import ssafy.ddada.domain.gym.command.GymSearchCommand;

public interface GymService {

    Page<GymSimpleResponse> getFilteredGyms(GymSearchCommand gymSearchCommand);
    GymDetailResponse getGymById(Long gymId);

    void createGym(GymCreateRequest request);

}
