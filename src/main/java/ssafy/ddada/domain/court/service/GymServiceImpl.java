package ssafy.ddada.domain.court.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.gym.response.GymDetailResponse;
import ssafy.ddada.domain.court.entity.Gym;
import ssafy.ddada.domain.court.repository.GymRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymServiceImpl implements GymService {

    private final GymRepository gymRepository;

    @Override
    public GymDetailResponse getGymInfo(Long gymAdminId) {
        log.info("getGymInfo >>>> gymAdminId: {}", gymAdminId);
        Gym gym = gymRepository.getGymsById(gymAdminId);
        return GymDetailResponse.from(gym);
    }

}
