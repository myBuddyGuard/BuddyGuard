package buddyguard.mybuddyguard.weight.mapper;

import buddyguard.mybuddyguard.weight.entity.Weight;
import buddyguard.mybuddyguard.weight.contoller.request.WeightCreateRequest;
import buddyguard.mybuddyguard.weight.contoller.response.WeightResponse;
import java.util.List;

public class WeightMapper {

    public static WeightResponse toResponse(Weight weight) {
        return new WeightResponse(
                weight.getId(),
                weight.getPetId(),
                weight.getWeight(),
                weight.getRecordedAt(),
                "체중",
                weight.getDescription()
        );
    }

    public static List<WeightResponse> toResponseList(List<Weight> weights) {
        return weights.stream()
                .map(WeightMapper::toResponse)
                .toList();
    }

    public static Weight toEntity(WeightCreateRequest request) {
        return new Weight(
                request.petId(),
                request.recordedAt(),
                request.weight(),
                request.description()
        );
    }
}
