package io.getarrays.securecapita.roles.prerunner;

import io.getarrays.securecapita.domain.Role;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.repository.UserRoleRepository;
import io.getarrays.securecapita.repository.implementation.RoleRepository1;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.roles.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RolesPreRunner implements CommandLineRunner {
    private final UserRepository1 userRepository1;
    private final RoleRepository1 roleRepository1;
    private final UserRoleRepository userRoleRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        List<Role> roles = roleRepository1.findAll();
        ArrayList<User> noRoleUsers = userRepository1.findUsersWithNoRoles();
        Optional<Role> role = roles.stream().filter(r -> r.getName().equals(AUTH_ROLE.USER.name())).findFirst();
        for (User user : noRoleUsers) {
            UserRole userRole = UserRole.builder().user(user).active(true).role(role.orElseGet(() -> roles.get(0))).createdDate(new Timestamp(System.currentTimeMillis())).build();
            user.addRole(userRoleRepository.save(userRole));
        }
        userRepository1.saveAll(noRoleUsers);
    }

    public void initializeRoles() {
        List<Role> roles = roleRepository1.findAll();
        String permissionUser = ROLE_AUTH.READ_USER + "," + ROLE_AUTH.REQUEST_MOVE_ASSET; // Added REQUEST_MOVE_ASSET to user permissions;
        updateRole(roles, AUTH_ROLE.USER, permissionUser);

        String permissionAdmin = ROLE_AUTH.READ_USER + "," + //who can request and approve admin, all admin?yes
//                ROLE_AUTH.UPDATE_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.VIEW_STATION + "," +
//                ROLE_AUTH.ASSIGN_ROLE + "," +
//                ROLE_AUTH.REQUEST_MOVE_ASSET + "," +
                ROLE_AUTH.APPROVE_MOVE_ASSET + "," +

                ROLE_AUTH.CREATE_ASSET;
        updateRole(roles, AUTH_ROLE.ADMIN, permissionAdmin)
        ;
//        String permissionAdminCT = ROLE_AUTH.READ_USER + "," + //who can request and approve admin, all admin?yes
////                ROLE_AUTH.UPDATE_USER + "," +
//                ROLE_AUTH.VIEW_ASSET + "," +
//                ROLE_AUTH.VIEW_STATION + "," +
////                ROLE_AUTH.ASSIGN_ROLE + "," +
////                ROLE_AUTH.REQUEST_MOVE_ASSET + "," +
//                ROLE_AUTH.APPROVE_MOVE_ASSET + "," +
//                ROLE_AUTH.CREATE_PRODUCT + "," +
//                ROLE_AUTH.CREATE_ASSET;
//        updateRole(roles, AUTH_ROLE.AdminCS, permissionAdminCS)
//        ;


        String permissionSysAdmin = ROLE_AUTH.READ_USER + "," +
                ROLE_AUTH.UPDATE_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.VIEW_STATION + "," +
                ROLE_AUTH.ALL_STATION + "," +
                ROLE_AUTH.REQUEST_MOVE_ASSET + "," +
                ROLE_AUTH.APPROVE_MOVE_ASSET + "," +
                ROLE_AUTH.CREATE_STATION + "," +
                ROLE_AUTH.ASSIGN_STATION + "," +
                ROLE_AUTH.ASSIGN_ROLE + "," +
                ROLE_AUTH.CREATE_PRODUCT + "," +
                ROLE_AUTH.CREATE_PURCHASEREQUEST + "," +
                ROLE_AUTH.CREATE_ASSET;
        updateRole(roles, AUTH_ROLE.SYSADMIN, permissionSysAdmin);


        String permissionDEPUTY_HEAD_IT = ROLE_AUTH.READ_USER + "," +
                ROLE_AUTH.UPDATE_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.VIEW_STATION + "," +
                ROLE_AUTH.ALL_STATION + "," +
                ROLE_AUTH.REQUEST_MOVE_ASSET + "," +
                ROLE_AUTH.APPROVE_MOVE_ASSET + "," +
                ROLE_AUTH.CREATE_STATION + "," +
                ROLE_AUTH.ASSIGN_STATION + "," +
                ROLE_AUTH.ASSIGN_ROLE + "," +
                ROLE_AUTH.CREATE_PRODUCT + "," +
                ROLE_AUTH.CREATE_PURCHASEREQUEST + "," +
                ROLE_AUTH.CREATE_ASSET;
        updateRole(roles, AUTH_ROLE.DEPUTY_HEAD_IT,permissionDEPUTY_HEAD_IT);













        String permissionAdminCS = ROLE_AUTH.READ_USER + "," +
                ROLE_AUTH.UPDATE_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.VIEW_STATION + "," +
//                ROLE_AUTH.ALL_STATION + "," +
                ROLE_AUTH.REQUEST_MOVE_ASSET + "," +
//                ROLE_AUTH.APPROVE_MOVE_ASSET + "," +
                ROLE_AUTH.CREATE_STATION + "," +
                ROLE_AUTH.ASSIGN_STATION + "," +
                ROLE_AUTH.ASSIGN_ROLE + "," +
                ROLE_AUTH.CREATE_PRODUCT + "," +
                ROLE_AUTH.CREATE_PURCHASEREQUEST + "," +
                ROLE_AUTH.CREATE_ASSET;
        updateRole(roles, AUTH_ROLE.AdminCS, permissionAdminCS);


        ;


        String permissionAdminOfficer = ROLE_AUTH.READ_USER + "," +
                ROLE_AUTH.UPDATE_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.VIEW_STATION + "," +
                ROLE_AUTH.ALL_STATION + "," +
                ROLE_AUTH.REQUEST_MOVE_ASSET + "," +
                ROLE_AUTH.APPROVE_MOVE_ASSET + "," +
                ROLE_AUTH.CREATE_STATION + "," +
                ROLE_AUTH.ASSIGN_STATION + "," +
                ROLE_AUTH.ASSIGN_ROLE + "," +
                ROLE_AUTH.CREATE_PRODUCT + "," +
                ROLE_AUTH.CREATE_PURCHASEREQUEST + "," +
                ROLE_AUTH.CREATE_ASSET;
        updateRole(roles, AUTH_ROLE.ADMINOFFICER, permissionAdminCS);




        String permissionSOFTWARE_DEVELOPER = ROLE_AUTH.READ_USER + "," +
                ROLE_AUTH.UPDATE_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.VIEW_STATION + "," +
                ROLE_AUTH.ALL_STATION + "," +
                ROLE_AUTH.REQUEST_MOVE_ASSET + "," +
                ROLE_AUTH.APPROVE_MOVE_ASSET + "," +
                ROLE_AUTH.CREATE_STATION + "," +
                ROLE_AUTH.ASSIGN_STATION + "," +
                ROLE_AUTH.ASSIGN_ROLE + "," +
                ROLE_AUTH.CREATE_PRODUCT + "," +
                ROLE_AUTH.CREATE_PURCHASEREQUEST + "," +
                ROLE_AUTH.CREATE_ASSET;
        updateRole(roles, AUTH_ROLE.SOFTWARE_DEVELOPER,permissionSOFTWARE_DEVELOPER );








        String permissionAuditor = ROLE_AUTH.READ_USER + "," +

                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.VIEW_STATION + "," +

                ROLE_AUTH.VIEW_STATION;
        updateRole(roles, AUTH_ROLE.AUDITOR, permissionAuditor);


        String permissionOFFICEBEARER = ROLE_AUTH.READ_USER + "," +

                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.VIEW_STATION + "," +
                ROLE_AUTH.REQUEST_MOVE_ASSET + "," +
                ROLE_AUTH.VIEW_STATION;
        updateRole(roles, AUTH_ROLE.ASSISTANT_ADMIN, permissionOFFICEBEARER);

        String permissionSecretary = ROLE_AUTH.READ_USER + "," +

                ROLE_AUTH.VIEW_ASSET + "," +
                              ROLE_AUTH.ALL_STATION;
        updateRole(roles, AUTH_ROLE.SECRETARY, permissionSecretary);

        String permissionHEADADMIN = ROLE_AUTH.READ_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.ALL_STATION;
        updateRole(roles, AUTH_ROLE.HEADADMIN, permissionHEADADMIN);
        updateRole(roles, AUTH_ROLE.HEAD_IT, permissionHEADADMIN);
        updateRole(roles, AUTH_ROLE.DEPUTY_HEAD_IT, permissionHEADADMIN);
        updateRole(roles, AUTH_ROLE.SOFTWARE_DEVELOPER, permissionHEADADMIN);
        String permissionDEPUTYHEADADMIN = ROLE_AUTH.READ_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.ALL_STATION + "," +
                ROLE_AUTH.CHECK_ASSET + "," +
                ROLE_AUTH.APPROVE_MOVE_ASSET+","+

                ROLE_AUTH.VIEW_STATION;
        updateRole(roles, AUTH_ROLE.DEPUTYHEADADMIN, permissionDEPUTYHEADADMIN);
        String permissionASS_Secretary = ROLE_AUTH.READ_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.ALL_STATION ;
        updateRole(roles, AUTH_ROLE.ASSISTANT_SECRETARY, permissionASS_Secretary);


        String permissionPRINCIPAL_ADMIN = ROLE_AUTH.READ_USER + "," +
//                ROLE_AUTH.UPDATE_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.VIEW_STATION + "," +
               ROLE_AUTH.REQUEST_MOVE_ASSET;
        updateRole(roles, AUTH_ROLE. PRINCIPAL_ADMIN, permissionPRINCIPAL_ADMIN);

        String permissionRegistrar = ROLE_AUTH.READ_USER + "," +
                ROLE_AUTH.VIEW_ASSET + "," +
                ROLE_AUTH.CHECK_ASSET + "," +
                ROLE_AUTH.VIEW_STATION;
        updateRole(roles, AUTH_ROLE.REGISTRAR, permissionRegistrar);
        updateRole(roles, AUTH_ROLE.DEPUTY_REGISTRAR, permissionRegistrar);
    }

    //update role
    private void updateRole(List<Role> roles, AUTH_ROLE authRole, String authorities) {

        if (roles.stream().anyMatch((role -> Objects.equals(role.getName(), authRole.name())))) {
            Role role = roles.stream().filter((role1 -> Objects.equals(role1.getName(), authRole.name()))).findFirst().get();
            role.setName(authRole.name());
            role.setPermission(
                    authorities);
            roleRepository1.save(role);
        } else {
            Role roleUser = Role.builder().name(authRole.name()).permission(
                    authorities).build();
            roleRepository1.save(roleUser);
        }
    }
}