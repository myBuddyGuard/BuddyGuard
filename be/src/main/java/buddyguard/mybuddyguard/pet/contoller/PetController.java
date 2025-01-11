package buddyguard.mybuddyguard.pet.contoller;

import buddyguard.mybuddyguard.login.dto.CustomOAuth2User;
import buddyguard.mybuddyguard.pet.contoller.request.PetRegisterRequest;
import buddyguard.mybuddyguard.pet.contoller.request.PetUpdateInformationRequest;
import buddyguard.mybuddyguard.pet.contoller.response.PetDetailResponse;
import buddyguard.mybuddyguard.pet.contoller.response.PetWithUserListResponse;
import buddyguard.mybuddyguard.pet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pet")
public class PetController {

    private final PetService service;

    @Operation(summary = "펫을 등록하는 api", description = "유저에게 새로운 펫을 등록합니다")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerPet(
            @RequestPart(name = "data") PetRegisterRequest petRegisterRequest,
            @RequestPart(name = "image", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getId();
        service.register(petRegisterRequest, imageFile, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "유저의 모든 펫 정보를 가져오는 api", description = "유저와 연결된 모든 펫(최대 3마리) 리스트를 조회합니다")
    @GetMapping()
    public ResponseEntity<List<PetWithUserListResponse>> getPetWithUser(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getId();
        List<PetWithUserListResponse> response = service.getPetWithUser(userId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "펫 한 마리의 정보를 가져오는 api", description = "유저의 펫들 중에서 1마리의 정보만을 조회합니다")
    @GetMapping("/{petId}")
    public ResponseEntity<PetDetailResponse> getOnePetWithUser(
            @PathVariable("petId") Long petId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getId();
        PetDetailResponse response = service.getOnePetWithUser(userId, petId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "펫을 삭제하는 api", description = "유저가 등록한 펫의 정보를 삭제합니다. 호스트 유저가 펫을 삭제하는 경우, 게스트 유저들과 해당 펫의 연결도 모두 삭제됩니다")
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable("petId") Long petId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getId();
        service.delete(userId, petId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "유저의 펫의 정보를 업데이트하는 api", description = "유저와 연결된 펫 중 한마리의 정보를 업데이트합니다")
    @PatchMapping("/{petId}")
    public ResponseEntity<Void> updatePetInformation(@PathVariable("petId") Long petId,
            @RequestBody PetUpdateInformationRequest petUpdateInformationRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getId();
        service.update(userId, petId, petUpdateInformationRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "유저의 펫의 프로필 이미지를 업데이트하는 api", description = "유저와 연결된 펫 중 한마리의 프로필 이미지를 업데이트합니다")
    @PutMapping(value = "/{petId}/profileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePetProfileImage(@PathVariable("petId") Long petId,
            @RequestPart(name = "image") MultipartFile imageFile,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getId();
        service.updateProfileImage(userId, petId, imageFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
