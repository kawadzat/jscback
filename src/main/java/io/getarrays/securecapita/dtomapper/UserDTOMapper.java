package io.getarrays.securecapita.dtomapper;

import io.getarrays.securecapita.department.dto.DepartmentDto;
import io.getarrays.securecapita.domain.Role;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.roles.UserRole;
import io.getarrays.securecapita.stationsassignment.UserStationDto;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 8/28/2022
 */

public class UserDTOMapper {
    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);

        if (!CollectionUtils.isEmpty(user.getUserDepartments())) {
            DepartmentDto departmentDTO = new DepartmentDto(); // Initialize DTO
            userDTO.setDepartments(user.getUserDepartments().stream().map(e->DepartmentDto.toDto(e.getDepartment())).collect(Collectors.toList())); // Set to UserDTO
        }

        if (!CollectionUtils.isEmpty(user.getStations())) {
            userDTO.setStations(UserStationDto.fromEntities(user.getStations())); // Set to UserDTO
        }
        return userDTO;
    }


    public static UserDTO fromUser(User user, UserRole role) {
        UserDTO userDTO = new UserDTO();
        Hibernate.initialize(user.getRoles());
        Hibernate.initialize(user.getStations());
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoleName(role.getRole().getName());
        userDTO.setPermissions(role.getRole().getPermission());
        userDTO.setAssigned(!user.getStations().isEmpty());
        userDTO.setStations(UserStationDto.fromEntities(user.getStations()));
        if (!CollectionUtils.isEmpty(user.getUserDepartments())) {
            DepartmentDto departmentDTO = new DepartmentDto(); // Initialize DTO
            userDTO.setDepartments(user.getUserDepartments().stream().map(e->DepartmentDto.toDto(e.getDepartment())).collect(Collectors.toList())); // Set to UserDTO
        }
        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}

















