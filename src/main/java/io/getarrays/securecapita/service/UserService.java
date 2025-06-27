package io.getarrays.securecapita.service;

import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.domain.HttpResponse;
import io.getarrays.securecapita.domain.Role;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.domain.UserRegisterDto;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.form.UpdateForm;
import io.getarrays.securecapita.resource.ResetPasswordDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 8/28/2022
 */
public interface UserService {

    ResponseEntity<?> createUser(UserRegisterDto user);


//    int getNumberOfpgaes(int pageSize);


    boolean deleteUser(Long id);


    UserDTO listUsers();


    Collection<UserDTO> list();

    UserDTO getUserByEmail(String email);


    void sendVerificationCode(UserDTO user);

    void resetPassword(String email);

    UserDTO verifyPasswordKey(String key);

    void renewPassword(String key, String password, String confirmPassword);

    UserDTO verifyAccountKey(String key);

    UserDTO updateUserDetails(UpdateForm user);

    UserDTO getUserById(Long userId);


    void updatePassword(Long userId, String currentPassword, String newPassword, String confirmNewPassword);

    void updateUserRole(Long userId, String roleName);

    void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked);

    UserDTO toggleMfa(String email);

    void updateImage(UserDTO user, MultipartFile image);


    UserDTO verifyCode(String email, String code);


    ResponseEntity<?> changeRole(Long userId, String role);

    ResponseEntity<?> resetpassword(ResetPasswordDto resetPassword);
}
