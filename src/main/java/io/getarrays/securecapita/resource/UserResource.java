package io.getarrays.securecapita.resource;

import io.getarrays.securecapita.domain.HttpResponse;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.domain.UserPrincipal;
import io.getarrays.securecapita.domain.UserRegisterDto;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.event.NewUserEvent;
import io.getarrays.securecapita.exception.ApiException;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.form.LoginForm;
import io.getarrays.securecapita.form.SettingsForm;
import io.getarrays.securecapita.form.UpdateForm;
import io.getarrays.securecapita.form.UpdatePasswordForm;
import io.getarrays.securecapita.provider.TokenProvider;
import io.getarrays.securecapita.service.EventService;
import io.getarrays.securecapita.service.RoleService;
import io.getarrays.securecapita.service.UserService;
import io.getarrays.securecapita.service.implementation.UserService1;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static io.getarrays.securecapita.dtomapper.UserDTOMapper.toUser;
import static io.getarrays.securecapita.enumeration.EventType.*;
import static io.getarrays.securecapita.utils.ExceptionUtils.processError;
import static io.getarrays.securecapita.utils.UserUtils.getAuthenticatedUser;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Junior RT
 * @version 1.0
 * @since 3/24/2023
 */

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {
    private static final String TOKEN_PREFIX = "Bearer ";
    private final UserService userService;
    private final UserService1 userService1;
    private final RoleService roleService;
    private final EventService eventService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginForm loginForm) {
        return authenticate(loginForm.getEmail(), loginForm.getPassword());
    }

    //method to get user by id
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO> getByUserId(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
////, @RequestParam("pageSize") int pageSize//\
    //,"pageSize", pageSize

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> listUsers(Authentication authentication) {
        // int totalPage = userService.getNumberOfpgaes(pageSize);
        // whate example pagination will follow.10 user pr
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("users", userService.list()))
                        .message("Users Retrieved")
                        .build()
        );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("message", "User Deleted"))
                        .message("User Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    //
//    @GetMapping("/pages")
//    public ResponseEntity<Integer> getNumberOfPages(@RequestParam("pageSize") int pageSize, @RequestParam("size") int size) {
//        int numberOfPages = userService.getNumberOfpgaes(pageSize);//            getNumberOfPages(pageSize, size);
//        // what requiremnet for respones sir??. i want to paginate users,any thing is ok,as long as it works
//        // for the query is only get total page for user sir need to adjust ok just do what you think works
//        // okay sir
//        // where ure api use for display list user sir
//        return ResponseEntity.ok(numberOfPages);
//    }
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody @Valid UserRegisterDto userDto) {
        return userService.createUser(userDto);
    }


    @GetMapping("/profile")//this pull roles and name of person together
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        UserDTO user = userService.getUserByEmail(getAuthenticatedUser(authentication).getEmail());
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user, "events", eventService.getEventsByUserId(user.getId()), "roles", roleService.getRoles()))
                        .message("Profile Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update")
    public ResponseEntity<HttpResponse> updateUser(@RequestBody @Valid UpdateForm user) {
        UserDTO updatedUser = userService.updateUserDetails(user);
        publisher.publishEvent(new NewUserEvent(updatedUser.getEmail(), PROFILE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", updatedUser, "events", eventService.getEventsByUserId(user.getId()), "roles", roleService.getRoles()))
                        .message("User updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
//    @GetMapping("/verify/code/{email}/{code}")
//    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
//        UserDTO user = userService.verifyCode(email, code);
//        publisher.publishEvent(new NewUserEvent(user.getEmail(), LOGIN_ATTEMPT_SUCCESS));
//        return ResponseEntity.ok().body(
//                HttpResponse.builder()
//                        .timeStamp(now().toString())
//                        .data(of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user))
//                                , "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
//                        .message("Login Success")
//                        .status(OK)
//                        .statusCode(OK.value())
//                        .build());
//    }


    // START - To reset password when user is not logged in

    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        UserDTO user = userService.verifyCode(email, code);
        publisher.publishEvent(new NewUserEvent(user.getEmail(), LOGIN_ATTEMPT_SUCCESS));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user))
                                , "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
                        .message("Login Success")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }


    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Email sent. Please check your email to reset your password.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/verify/password/{key}")
    public ResponseEntity<HttpResponse> verifyPasswordUrl(@PathVariable("key") String key) {
        UserDTO user = userService.verifyPasswordKey(key);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user))
                        .message("Please enter a new password")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/resetpassword/{key}/{password}/{confirmPassword}")
    public ResponseEntity<HttpResponse> resetPasswordWithKey(@PathVariable("key") String key, @PathVariable("password") String password,
                                                             @PathVariable("confirmPassword") String confirmPassword) {
        userService.renewPassword(key, password, confirmPassword);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Password reset successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/reset/password")
    public ResponseEntity<?> resetPasswordWithKey(@Validated @RequestBody ResetPasswordDto resetPassword) {
        return userService.resetpassword(resetPassword);
    }

    // END - To reset password when user is not logged in

    @PatchMapping("/update/password")
    public ResponseEntity<HttpResponse> updatePassword(Authentication authentication, @RequestBody @Valid UpdatePasswordForm form) {
        UserDTO userDTO = getAuthenticatedUser(authentication);
        userService.updatePassword(userDTO.getId(), form.getCurrentPassword(), form.getNewPassword(), form.getConfirmNewPassword());
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), PASSWORD_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserById(userDTO.getId()), "events", eventService.getEventsByUserId(userDTO.getId()), "roles", roleService.getRoles()))
                        .message("Password updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/role/{roleName}")
    public ResponseEntity<HttpResponse> updateUserRole(Authentication authentication, @PathVariable("roleName") String roleName) {
        UserDTO userDTO = getAuthenticatedUser(authentication);
        userService.updateUserRole(userDTO.getId(), roleName);
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), ROLE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("user", userService.getUserById(userDTO.getId()), "events", eventService.getEventsByUserId(userDTO.getId()), "roles", roleService.getRoles()))
                        .timeStamp(now().toString())
                        .message("Role updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/settings")
    public ResponseEntity<HttpResponse> updateAccountSettings(Authentication authentication, @RequestBody @Valid SettingsForm form) {
        UserDTO userDTO = getAuthenticatedUser(authentication);
        userService.updateAccountSettings(userDTO.getId(), form.getEnabled(), form.getNotLocked());
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), ACCOUNT_SETTINGS_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("user", userService.getUserById(userDTO.getId()), "events", eventService.getEventsByUserId(userDTO.getId()), "roles", roleService.getRoles()))
                        .timeStamp(now().toString())
                        .message("Account settings updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
//
//    @PatchMapping("/togglemfa")
//    public ResponseEntity<HttpResponse> toggleMfa(Authentication authentication) throws InterruptedException {
//        TimeUnit.SECONDS.sleep(3);
//        UserDTO user = userService.toggleMfa(getAuthenticatedUser(authentication).getEmail());
//        publisher.publishEvent(new NewUserEvent(user.getEmail(), MFA_UPDATE));
//        return ResponseEntity.ok().body(
//                HttpResponse.builder()
//                        .data(of("user", user, "events", eventService.getEventsByUserId(user.getId()), "roles", roleService.getRoles()))
//                        .timeStamp(now().toString())
//                        .message("Multi-Factor Authentication updated")
//                        .status(OK)
//                        .statusCode(OK.value())
//                        .build());
//    }

    @PatchMapping("/update/image")
    public ResponseEntity<HttpResponse> updateProfileImage(Authentication authentication, @RequestParam("image") MultipartFile image) throws InterruptedException {
        UserDTO user = getAuthenticatedUser(authentication);
        userService.updateImage(user, image);
        publisher.publishEvent(new NewUserEvent(user.getEmail(), PROFILE_PICTURE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("user", userService.getUserById(user.getId()), "events", eventService.getEventsByUserId(user.getId()), "roles", roleService.getRoles()))
                        .timeStamp(now().toString())
                        .message("Profile image updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping(value = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getProfileImage(@PathVariable("fileName") String fileName) throws Exception {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/" + fileName));
    }


    @GetMapping("/verify/account/{key}")
    public ResponseEntity<HttpResponse> verifyAccount(@PathVariable("key") String key) {


        //     TimeUnit.SECONDS.sleep(3);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message(userService.verifyAccountKey(key).isEnabled() ? "Account already verified" : "Account verified")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }


//    @GetMapping("/verify/password/{key}")
//    public ResponseEntity<HttpResponse> verifyPasswordUrl(@PathVariable("key") String key) throws InterruptedException {
//       TimeUnit.SECONDS.sleep(3);
//        UserDTO user = userService.verifyPasswordKey(key);
//        return ResponseEntity.ok().body(
//                HttpResponse.builder()
//                        .timeStamp(now().toString())
//                        .data(of("user", user))
//                        .message("Please enter a new password")
//                        .status(OK)
//                        .statusCode(OK.value())
//                        .build());
//    }

    // START - To reset password when user is not logged in


//    @GetMapping("/verify/code/{email}/{code}")
//    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
//        UserDTO user = userService.verifyCode(email, code);
//        publisher.publishEvent(new NewUserEvent(user.getEmail(), LOGIN_ATTEMPT_SUCCESS));
//        return ResponseEntity.ok().body(
//                HttpResponse.builder()
//                        .timeStamp(now().toString())
//                        .data(of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user))
//                                , "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
//                        .message("Login Success")
//                        .status(OK)
//                        .statusCode(OK.value())
//                        .build());
//    }


    @GetMapping("/refresh/token")
    public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
        if (isHeaderAndTokenValid(request)) {
            String token = request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length());
            UserDTO user = userService.getUserById(tokenProvider.getSubject(token, request));
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user))
                                    , "refresh_token", token))
                            .message("Token refreshed")
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
        } else {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .reason("Refresh Token missing or invalid")
                            .developerMessage("Refresh Token missing or invalid")
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build());
        }
    }


    private boolean isHeaderAndTokenValid(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) != null
                && request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                && tokenProvider.isTokenValid(
                tokenProvider.getSubject(request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()), request),
                request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length())
        );
    }


    @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                        .status(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .build());
    }

    /*@RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                .status(NOT_FOUND)
                .statusCode(NOT_FOUND.value())
                .build(), NOT_FOUND);
    }*/

    private ResponseEntity<?> authenticate(String email, String password) throws NoSuchElementException {
        User testUser = userService1.loadUserByUsername(email);
        if (testUser != null) {
            publisher.publishEvent(new NewUserEvent(LOGIN_ATTEMPT, testUser.getId()));
            if (passwordEncoder.matches(password, testUser.getPassword())) {

//            Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
//            UserDTO loggedInUser = getLoggedInUser(authentication);
//            if (!testUser.isUsingMfa()) {
//                publisher.publishEvent(new NewUserEvent(LOGIN_ATTEMPT_SUCCESS, testUser.getId()));
//            }
                UserDTO user = UserDTO.toDto(testUser);
                return user.isUsingMfa() ? sendVerificationCode(user) : sendResponse(user);
            }
        }

        publisher.publishEvent(new NewUserEvent(LOGIN_ATTEMPT_FAILURE, testUser.getId()));
        return ResponseEntity.status(401).body(new CustomMessage("Please check your username or password."));
        //        processError(request, response, new Exception("Unable to Login"));
//        throw new ApiException(new Exception("Unable to Login. please check your credentials again.").getMessage());
    }


    private URI getUri() {
        return URI.create(fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO user) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user))
                                , "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
                        .message("Login Success")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    private UserPrincipal getUserPrincipal(UserDTO user) {
        return new UserPrincipal(toUser(userService.getUserByEmail(user.getEmail())), roleService.getRoleByUserId(user.getId()));
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO user) {
        userService.sendVerificationCode(user);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user))
                        .message("Verification Code Sent")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }


//    @PostMapping("/assignStationToUser")
//    public ResponseEntity<?> assignStationToUser(
//            @RequestParam("userId") Long userId,
//            @RequestParam("stationId") Long stationId) {
//        return userService.addStationToUser(userId, stationId);
//    }


//    @PostMapping("/assignStationToUser")
//    public ResponseEntity<String> assignStationToUser(@RequestParam("userId") Long userId,
//                                                      @RequestParam("stationId") Integer stationId) {
//       userService.assignStationToUser(userId, stationId);
//
//        return ResponseEntity.ok("Station assigned to user successfully.");
////    }
//@PutMapping("/{userId}/assignStation/{stationId}")
//public ResponseEntity<String> assignStationToUser(@PathVariable("userId") Long userId,
//                                                  @PathVariable("stationId") Integer stationId) {
//userService.assignStationToUser(userId, stationId);
//    return ResponseEntity.ok("Station assigned to user successfully.");
//}

}



















