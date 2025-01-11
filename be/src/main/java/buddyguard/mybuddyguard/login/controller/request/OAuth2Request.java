package buddyguard.mybuddyguard.login.controller.request;

public interface OAuth2Request {
    String getEmail();
    String getName();
    String getProviderId();
    String getProfileImage();
}
