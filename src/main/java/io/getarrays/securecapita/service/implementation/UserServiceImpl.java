package io.getarrays.securecapita.service.implementation;

import io.getarrays.securecapita.domain.HttpResponse;
import io.getarrays.securecapita.domain.Role;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.domain.UserRegisterDto;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.form.UpdateForm;
import io.getarrays.securecapita.repository.RoleRepository;
import io.getarrays.securecapita.repository.UserRepository;
import io.getarrays.securecapita.repository.UserRoleRepository;
import io.getarrays.securecapita.repository.implementation.RoleRepository1;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.resource.ResetPasswordDto;
import io.getarrays.securecapita.roles.UserRole;
import io.getarrays.securecapita.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static io.getarrays.securecapita.dtomapper.UserDTOMapper.fromUser;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 8/28/2022
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository1 userRepository1;
    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRoleRepository;
    private final RoleRepository1 roleRepository1;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    @Override
    public boolean deleteUser(Long id) {

        return userRepository.delete(id);
    }
//    @Override
//    public int getNumberOfpgaes(int pageSize) {
////        int totalNumberOfItems = size; // Replace with the actual total number of items
////        int numberOfPages = (int) Math.ceil((double) totalNumberOfItems / pageSize);
//        return userRepository.getNumberOfPages(pageSize);
//    }

    @Override
    public ResponseEntity<?> createUser(UserRegisterDto userDto) {
        // Check if the user already exists by username
        Optional<User> existingUser = userRepository1.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(422).body(new CustomMessage("A user with the email '" + userDto.getEmail() + "' already exists."));
        }

        List<Role> roles = roleRepository1.findAll();
        Optional<Role> role = roles.stream().filter(r -> r.getName().equals("USER")).findFirst();
        UserRole userRole = UserRole.builder().active(true).role(role.orElseGet(() -> roles.get(0))).createdDate(new Timestamp(System.currentTimeMillis())).build();
        User user=new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setNotLocked(true);
        user.setUsingMfa(false);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository1.save(user);
        userRole.setUser(savedUser);
        savedUser.addRole(userRoleRepository.save(userRole));
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", mapToUserDTO(userRepository1.save(user))))
                        .message("User created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }


    @Override
    public UserDTO listUsers() {


        return mapToUserDTO((User) userRepository.findAll());
    }


    @Override
    @Transactional
    public Collection<UserDTO> list() {
        return maptoUserDTOList(userRepository1.findAll());
    }

    private Collection<UserDTO> maptoUserDTOList(Collection<User> users) {
        Collection<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(user -> {
            Hibernate.initialize(user.getStations());
            Hibernate.initialize(user.getRoles());
            userDTOS.add(fromUser(user, userRoleRepository.getRoleByUserId(user.getId())));
        });
        return userDTOS;
    }


    @Override
    public UserDTO getUserByEmail(String email) {
        return mapToUserDTO(userRepository1.findByEmail(email).get());
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        userRepository.sendVerificationCode(user);
    }

    @Override
    public UserDTO verifyCode(String email, String code) {
        return mapToUserDTO(userRepository.verifyCode(email, code));
    }

    @Override
    @Transactional
    public ResponseEntity<?> changeRole(Long userId, String role) {
        System.out.println(role);
        Optional<Role> roleOptional = roleRepository1.findByRoleName(role);
        if (roleOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new CustomMessage("Role is invalid."));
        }
        Optional<User> userOptional = userRepository1.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new CustomMessage("UserId is invalid."));
        }
        User user = userOptional.get();
        user.expireAllRoles();
        userRoleRepository.deleteAll(user.getRoles());
        user.removeAllRole();
        UserRole userRole = UserRole.builder().user(user).active(true).createdDate(new Timestamp(System.currentTimeMillis())).role(roleOptional.get()).build();
        user.addRole(userRoleRepository.save(userRole));
        userRepository1.save(user);
        return ResponseEntity.ok(new CustomMessage("Role updated for User: " + user.getFirstName()));
    }


    @Override
    public void resetPassword(String email) {
        userRepository.resetPassword(email);
    }

    @Override
    public UserDTO verifyPasswordKey(String key) {
        return mapToUserDTO(userRepository.verifyPasswordKey(key));
    }

    @Override
    public void renewPassword(String key, String password, String confirmPassword) {
        userRepository.renewPassword(key, password, confirmPassword);
    }

    @Override
    public UserDTO verifyAccountKey(String key) {
        return mapToUserDTO(userRepository.verifyAccountKey(key));
    }

    @Override
    public UserDTO updateUserDetails(UpdateForm user) {
        return mapToUserDTO(userRepository.updateUserDetails(user));
    }

    @Override
    @Transactional
    public UserDTO getUserById(Long userId) {
        User user=userRepository1.findById(userId).get();
        Hibernate.initialize(user.getRoles());
        return mapToUserDTO(user);
    }


    @Override
    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        userRepository.updatePassword(id, currentPassword, newPassword, confirmNewPassword);
    }


//    public void assignStationToUser(Long userId, Integer stationId) {
//        userRepository1.addStationToUser(userId, stationId);
//    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
        roleRoleRepository.updateUserRole(userId, roleName);
    }

    @Override
    public void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked) {
        userRepository.updateAccountSettings(userId, enabled, notLocked);
    }

    @Override
    public UserDTO toggleMfa(String email) {
        return mapToUserDTO(userRepository.toggleMfa(email));
    }

    @Override
    public void updateImage(UserDTO user, MultipartFile image) {
        userRepository.updateImage(user, image);
    }


    @Override
    public ResponseEntity<?> resetpassword(ResetPasswordDto resetPassword) {
        if(resetPassword.getConfirmPassword().equals(resetPassword.getPassword())){
            Optional<User> userOptional=userRepository1.findByPasswordVerificationToken(resetPassword.getToken());
            if(userOptional.isPresent()){
                userOptional.get().setPassword(passwordEncoder.encode(resetPassword.getPassword()));
                userOptional.get().setVerificationToken(null);
                userRepository1.save(userOptional.get());
                return ResponseEntity.ok(new CustomMessage("Password changed, you can login."));
            }
            return ResponseEntity.badRequest().body(new CustomMessage("Your token is not valid."));
        }
        return ResponseEntity.badRequest().body(new CustomMessage("Confirm password do not match."));
    }

    private UserDTO mapToUserDTO(User user) {
        System.out.println(user.getRoles());
        return fromUser(user, userRoleRepository.getRoleByUserId(user.getId()));
    }
}
















