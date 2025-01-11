package buddyguard.mybuddyguard.weight;

import static org.assertj.core.api.Assertions.*;

import buddyguard.mybuddyguard.exception.InvalidRequestException;
import buddyguard.mybuddyguard.weight.entity.Weight;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeightTest {

    private Weight weight;
    private static final Long OWNER_ID = 1L;
    private static final Long INVALID_OWNER_ID = 2L;
    private static final String INVALID_REQUEST_MESSAGE = "잘못된 요청 입니다.";

    @BeforeEach
    void setUp() {
        weight = new Weight(OWNER_ID, LocalDateTime.now(), 10.0, "hello");
    }

    @Test
    void 소유권이_일치하면_예외가_발생하지_않는다() {
        weight.validateOwnership(OWNER_ID);
    }

    @Test
    void 소유권이_일치하지_않으면_예외가_발생한다() {
        assertThatThrownBy(() -> weight.validateOwnership(INVALID_OWNER_ID))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(INVALID_REQUEST_MESSAGE);
    }
}
