package io.getarrays.securecapita.service.implementation;

import io.getarrays.securecapita.domain.Role;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.repository.RoleRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 12/26/2022
 */

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository<Role> roleRoleRepository;
    private final UserRepository1 userRepository1;

    @Override
    public Role getRoleByUserId(Long id) {
        return roleRoleRepository.getRoleByUserId(id);
    }

    @Override
    public Collection<Role> getRoles() {
        return roleRoleRepository.list();
    }



//    public  void assignUserRole(Long userId,Long roleId){

//User user = userRepository1.getById(userId).orElse(null);
//Role role=roleRoleRepository.findById(roleId);
//
//        Optional<User> userOptional = userRepository1.findById(userId);
//        if (!userOptional.isPresent()) {
//            throw new IllegalArgumentException("User not found with id: " + userId);
//        }
//        User user = userOptional.get();
//
//        // Fetch the Role entity
//        Optional<Role> roleOptional = roleRoleRepository.findById(roleId);
//        if (!roleOptional.isPresent()) {
//            throw new IllegalArgumentException("Role not found with id: " + roleId);
//        }
//        Role role = roleOptional.get();
//
//        // Add the role to the user's roles
//        user.addRole(role);
//
//        // Save the updated user entity
//        userRepository1.save(user);
//


 //   }

}
