package ssafy.ddada.domain.gym.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.api.gym.request.GymCreateRequest;
import ssafy.ddada.api.gym.response.GymDetailResponse;
import ssafy.ddada.api.gym.response.GymSimpleResponse;
import ssafy.ddada.common.exception.gym.GymNotFoundException;
import ssafy.ddada.common.util.S3Util;
import ssafy.ddada.domain.gym.command.GymSearchCommand;
import ssafy.ddada.domain.gym.entity.Gym;
import ssafy.ddada.domain.gym.repository.GymRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GymServiceImpl implements GymService {

    private final GymRepository gymRepository;
    private final S3Util s3Util;

    @Override
    public Page<GymSimpleResponse> getFilteredGyms(GymSearchCommand command) {
        return gymRepository
                .findGymsByKeywordAndRegion(command.keyword(), command.regions(), command.pageable())
                .map(gym -> {
                    String image = Objects.requireNonNull(gym.getImage());
                    String presignedUrl = s3Util.getPresignedUrlFromS3(image);
                    gym.setImage(presignedUrl);
                    return gym;
                })
                .map(GymSimpleResponse::from);
    }

    @Override
    public GymDetailResponse getGymById(Long gymId) {
        Gym gym = gymRepository.findGymWithMatchesById(gymId)
                .orElseThrow(GymNotFoundException::new);

        String presignedUrl = s3Util.getPresignedUrlFromS3(gym.getImage());  // S3Util의 메서드 사용
        gym.setImage(presignedUrl);
        return GymDetailResponse.from(gym);
    }

    private boolean isImageEmpty(MultipartFile image) {
        return image == null || image.isEmpty();
    }

    public void createGym(GymCreateRequest request) {
        Gym gym = gymRepository.save(
                Gym.createGym(
                        request.name(),
                        request.address(),
                        request.contactNumber(),
                        request.description(),
                        null,
                        request.url(),
                        request.region()
                )
        );

        if (!isImageEmpty(request.image())){
            String imageUrl = s3Util.uploadImageToS3(request.image(), gym.getId(), "gymImg/");
            gym.setImage(imageUrl);
        }
        gymRepository.save(gym);
    }
}

