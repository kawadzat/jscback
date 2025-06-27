package io.getarrays.securecapita.user;

import io.getarrays.securecapita.department.model.DepartmentEntity;
import io.getarrays.securecapita.department.service.DepartmentService;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.dtomapper.UserDTOMapper;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import nl.basjes.parse.useragent.utils.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserRepository1 userRepository;

    public UserDTO update(UserDTO currentUser, Long userId, UserUpdateDto userUpdateDto) {
        User user = findByIdOrThrow(userId);

        List<Long> newDepartmentIds = userUpdateDto.getDepartmentIds();

        if (CollectionUtils.isEmpty(newDepartmentIds)) {
            user.getUserDepartments().clear();
        } else {
            // Fetch new departments
            List<DepartmentEntity> newDepartments = departmentService.findDepartmentsByIdsOrThrow(newDepartmentIds);

            // Fetch existing user departments
            List<UserDepartment> existingUserDepartments = user.getUserDepartments();
            Map<Long, UserDepartment> existingDepartmentMap =
                    existingUserDepartments.stream().collect(Collectors.toMap(ud -> ud.getDepartment().getId(),
                            ud -> ud));

            // Determine departments to remove
            List<UserDepartment> departmentsToRemove =
                    existingUserDepartments.stream().filter(ud -> !newDepartmentIds.contains(ud.getDepartment().getId())).toList();

            // Remove old departments
            user.getUserDepartments().removeAll(departmentsToRemove);

            // Determine departments to add
            List<UserDepartment> departmentsToAdd =
                    newDepartments.stream().filter(dept -> !existingDepartmentMap.containsKey(dept.getId())) // Only
                            // add if not already
                            // assigned
                            .map(dept -> {
                                UserDepartment userDepartment = new UserDepartment();
                                userDepartment.setUser(user);
                                userDepartment.setDepartment(dept);
                                userDepartment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                                userDepartment.setAssignedBy(findByIdOrThrow(currentUser.getId()));
                                return userDepartment;
                            }).toList();

            // Add new departments
            user.getUserDepartments().addAll(departmentsToAdd);
        }

        return UserDTOMapper.fromUser(userRepository.save(user));
    }


    public List<UserDTO> getDepartmentAndStationFellows(UserDTO currentUser) {
        User user = userRepository.findById(currentUser.getId()).get();

        // Get department ID safely
        List<Long> departmentIds = (user.getUserDepartments() != null) ?
                user.getUserDepartments().stream().map(e -> e.getDepartment().getId()).toList() :
                Collections.emptyList();

        // Get station IDs safely
        List<Long> stationIds = (user.getStations() != null) ?
                user.getStations().stream().map(e -> e.getStation().getStation_id()).filter(Objects::nonNull).toList() : Collections.emptyList();

        // Fetch users
        List<User> fellowUsers = userRepository.findByDepartmentIdAndStationIdIn(currentUser.getId(),
                !departmentIds.isEmpty() ? departmentIds : null, !stationIds.isEmpty() ? stationIds : null);

        return fellowUsers.stream().map(UserDTOMapper::fromUser).toList();
    }


    public List<UserDTO> filterUsers(List<Long> stationIds, List<Long> departmentIds) {
        return userRepository.findByDepartmentIdAndStationIdIn(null, departmentIds, stationIds).stream().map(UserDTOMapper::fromUser).toList();
    }

    private User findByIdOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No User found by id: "
                + userId));
    }
}
