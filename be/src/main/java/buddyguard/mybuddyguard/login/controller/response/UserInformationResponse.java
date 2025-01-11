package buddyguard.mybuddyguard.login.controller.response;

import java.util.List;

public record UserInformationResponse(Long userId, String name, String email, String profileImage,
                                      List<Long> petsId) {

}
