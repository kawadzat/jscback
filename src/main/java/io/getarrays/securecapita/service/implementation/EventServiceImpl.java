package io.getarrays.securecapita.service.implementation;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.domain.UserEvent;
import io.getarrays.securecapita.enumeration.EventType;
import io.getarrays.securecapita.repository.EventRepository;
import io.getarrays.securecapita.service.EventService;
import io.getarrays.securecapita.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 3/21/2023
 */

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    @Override
    public Collection<UserEvent> getEventsByUserId(Long userId) {
        return eventRepository.getEventsByUserId(userId);
    }

    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        UserEvent userEvent= UserEvent.builder().type(eventType).createdAt(LocalDateTime.now()).device(device).ipAddress(ipAddress).userId(UserUtils.getAuthenticatedUser(SecurityContextHolder.getContext().getAuthentication()).getId()).build();
        eventRepository.save(userEvent);
    }

    @Override
    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {
        UserEvent userEvent= UserEvent.builder().type(eventType).createdAt(LocalDateTime.now()).device(device).ipAddress(ipAddress).userId(userId).build();
        eventRepository.save(userEvent);
    }
}
