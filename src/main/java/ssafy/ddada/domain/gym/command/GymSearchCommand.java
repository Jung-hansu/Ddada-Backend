package ssafy.ddada.domain.gym.command;

import org.springframework.data.domain.Pageable;
import ssafy.ddada.domain.gym.entity.Region;

import java.util.Set;

public record GymSearchCommand(
        String keyword,
        Set<Region> regions,
        Pageable pageable
) { }
