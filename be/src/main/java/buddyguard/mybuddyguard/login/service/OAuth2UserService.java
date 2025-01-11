package buddyguard.mybuddyguard.login.service;

import buddyguard.mybuddyguard.login.dto.CustomOAuth2User;
import buddyguard.mybuddyguard.login.controller.request.KakaoRequest;
import buddyguard.mybuddyguard.login.controller.request.OAuth2Request;
import buddyguard.mybuddyguard.login.dto.UserDto;
import buddyguard.mybuddyguard.login.entity.Users;
import buddyguard.mybuddyguard.login.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2UserService.class);
    private final UserRepository userRepository;

    public OAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Request oAuth2Request = new KakaoRequest(oAuth2User.getAttributes());
        String oAuthId = "kakao" + oAuth2Request.getProviderId();
        String defaultRole = "ROLE_USER";

        logger.info("로그인 유저 -> name : {}, id : {}, email : {} image : {}", oAuth2Request.getName(),
                oAuth2Request.getProviderId(), oAuth2Request.getEmail(),
                oAuth2Request.getProfileImage());

        Users checkUser = userRepository.findByOauthId(oAuthId);

        // 최초 로그인 회원이면 우리 DB에 저장
        if (checkUser == null) {
            Users user = new Users();
            user.setName(oAuth2Request.getName());
            user.setOauthId(oAuthId);
            user.setProfileImage(oAuth2Request.getProfileImage());
            user.setEmail(oAuth2Request.getEmail());
            user.setRole(defaultRole);

            userRepository.save(user);
            logger.info("DB에 {}번 {} 회원 저장완료", user.getOauthId(), user.getName());

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setRole(defaultRole);
            userDto.setName(user.getName());
            return new CustomOAuth2User(userDto);
        }
        UserDto userDto = new UserDto();
        userDto.setId(checkUser.getId());
        userDto.setRole(defaultRole);
        userDto.setName(checkUser.getName());

        return new CustomOAuth2User(userDto);
    }
}
