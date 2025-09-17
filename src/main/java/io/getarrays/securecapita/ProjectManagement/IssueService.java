package io.getarrays.securecapita.ProjectManagement;

import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.exception.BadRequestException;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.itinventory.SignatureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueAcknowledgmentRepository issueAcknowledgmentRepository;
    private final UserRepository1 userRepository1;
    private final SignatureService signatureService;

    public IssueDto createIssue(UserDTO currentUser, IssueDto issueDto) {
        Issue issue = dtoToEntity(currentUser, null, issueDto);
        
        // Set default values
        if (issue.getStatus() == null) {
            issue.setStatus(IssueStatus.OPEN);
        }
        if (issue.getPriority() == null) {
            issue.setPriority(IssuePriority.MEDIUM);
        }
        if (issue.getIssueType() == null) {
            issue.setIssueType(IssueType.BUG);
        }
        
        Issue savedIssue = issueRepository.save(issue);
        return entityToDto(savedIssue);
    }

    public IssueDto updateIssue(UserDTO currentUser, Long issueId, IssueDto issueDto) {
        Issue existingIssue = findIssueByIdOrThrow(issueId);
        Issue updatedIssue = dtoToEntity(currentUser, issueId, issueDto);
        
        Issue savedIssue = issueRepository.save(updatedIssue);
        return entityToDto(savedIssue);
    }

    public IssueDto getIssueById(Long issueId) {
        Issue issue = findIssueByIdOrThrow(issueId);
        return entityToDto(issue);
    }

    public List<IssueDto> getAllIssues() {
        List<Issue> issues = issueRepository.findAll();
        return issues.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public List<IssueDto> getIssuesByStatus(IssueStatus status) {
        List<Issue> issues = issueRepository.findByStatus(status);
        return issues.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public IssueDto changeIssueStatus(UserDTO currentUser, Long issueId, IssueStatus newStatus) {
        Issue issue = findIssueByIdOrThrow(issueId);
        
        // If changing to ACKNOWLEDGED status, automatically create pending acknowledgment
        if (newStatus == IssueStatus.PENDING_ACKNOWLEDGMENT) {
            issue.setStatus(IssueStatus.PENDING_ACKNOWLEDGMENT);
            Issue savedIssue = issueRepository.save(issue);
            return entityToDto(savedIssue);
        }
        
        issue.setStatus(newStatus);
        Issue savedIssue = issueRepository.save(issue);
        return entityToDto(savedIssue);
    }

    public IssueDto assignIssue(UserDTO currentUser, Long issueId, Long assigneeId) {
        Issue issue = findIssueByIdOrThrow(issueId);
        
        User assignee = userRepository1.findById(assigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with id: " + assigneeId));
        
        issue.setAssignee(assignee);
        
        // Set status to PENDING_ACKNOWLEDGMENT when issue is assigned
        issue.setStatus(IssueStatus.PENDING_ACKNOWLEDGMENT);
        
        Issue savedIssue = issueRepository.save(issue);
        return entityToDto(savedIssue);
    }

    public IssueAcknowledgmentDto acknowledgeIssue(UserDTO currentUser, Long issueId, IssueAcknowledgmentDto acknowledgmentDto) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found with id: " + issueId));
        
        // Check if issue is in pending acknowledgment status
        if (issue.getStatus() != IssueStatus.PENDING_ACKNOWLEDGMENT) {
            throw new BadRequestException("Issue is not in pending acknowledgment status");
        }
        
        User user = userRepository1.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Generate signature hash if signature is provided
        String signatureHash = null;
        if (acknowledgmentDto.getSignature() != null && !acknowledgmentDto.getSignature().trim().isEmpty()) {
            signatureHash = signatureService.generateSignatureHash(
                acknowledgmentDto.getSignature(), 
                issueId, 
                currentUser.getId()
            );
        }
        
        // Create acknowledgment
        IssueAcknowledgment acknowledgment = IssueAcknowledgment.builder()
                .issue(issue)
                .acknowledgedBy(user)
                .acknowledgmentDate(LocalDateTime.now())
                .notes(acknowledgmentDto.getNotes())
                .signature(acknowledgmentDto.getSignature())
                .signatureType(acknowledgmentDto.getSignatureType())
                .signatureTimestamp(acknowledgmentDto.getSignatureTimestamp() != null ? acknowledgmentDto.getSignatureTimestamp() : LocalDateTime.now())
                .ipAddress(acknowledgmentDto.getIpAddress())
                .userAgent(acknowledgmentDto.getUserAgent())
                .certificateInfo(acknowledgmentDto.getCertificateInfo())
                .signatureHash(signatureHash)
                .build();
        
        IssueAcknowledgment savedAcknowledgment = issueAcknowledgmentRepository.save(acknowledgment);
        
        // Update issue status to ACKNOWLEDGED
        issue.setStatus(IssueStatus.ACKNOWLEDGED);
        issueRepository.save(issue);
        
        return acknowledgmentToDto(savedAcknowledgment);
    }

    public IssueDto manuallyAcknowledgeIssue(UserDTO currentUser, Long issueId, String notes) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found with id: " + issueId));

        // Only allow acknowledgment if status is PENDING_ACKNOWLEDGMENT
        if (issue.getStatus() != IssueStatus.PENDING_ACKNOWLEDGMENT) {
            throw new BadRequestException("Issue is not in PENDING_ACKNOWLEDGMENT status");
        }

        // Create acknowledgment record
        User user = userRepository1.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        IssueAcknowledgment acknowledgment = IssueAcknowledgment.builder()
                .issue(issue)
                .acknowledgedBy(user)
                .acknowledgmentDate(LocalDateTime.now())
                .notes(notes)
                .signatureType("ACKNOWLEDGMENT")
                .signatureTimestamp(LocalDateTime.now())
                .ipAddress("SYSTEM")
                .userAgent("SYSTEM_ACKNOWLEDGMENT")
                .certificateInfo("Acknowledged by user")
                .build();

        issueAcknowledgmentRepository.save(acknowledgment);

        // Update status to ACKNOWLEDGED
        issue.setStatus(IssueStatus.ACKNOWLEDGED);
        Issue savedIssue = issueRepository.save(issue);

        return entityToDto(savedIssue);
    }

    public List<IssueDto> getIssuesPendingAcknowledgment() {
        List<Issue> pendingIssues = issueRepository.findPendingAcknowledgmentIssues();
        return pendingIssues.stream()
                .map(laptop -> {
                    IssueDto dto = entityToDto(laptop);
                    // Get acknowledgment notes if available
                    IssueAcknowledgment acknowledgment = issueAcknowledgmentRepository.findByIssueId(laptop.getId()).orElse(null);
                    if (acknowledgment != null) {
                        dto.setNotes(acknowledgment.getNotes());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<IssueDto> getIssuesPendingAcknowledgmentByAssignee(Long assigneeId) {
        List<Issue> pendingIssues = issueRepository.findPendingAcknowledgmentIssuesByAssignee(assigneeId);
        return pendingIssues.stream()
                .map(laptop -> {
                    IssueDto dto = entityToDto(laptop);
                    // Get acknowledgment notes if available
                    IssueAcknowledgment acknowledgment = issueAcknowledgmentRepository.findByIssueId(laptop.getId()).orElse(null);
                    if (acknowledgment != null) {
                        dto.setNotes(acknowledgment.getNotes());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public IssueAcknowledgmentDto getIssueAcknowledgment(Long issueId) {
        IssueAcknowledgment acknowledgment = issueAcknowledgmentRepository.findByIssueId(issueId)
                .orElse(null);
        
        if (acknowledgment == null) {
            return null;
        }
        
        return acknowledgmentToDto(acknowledgment);
    }

    public void deleteIssue(Long issueId) {
        Issue issue = findIssueByIdOrThrow(issueId);
        issueRepository.delete(issue);
    }

    // Helper methods
    private Issue findIssueByIdOrThrow(Long issueId) {
        return issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found with id: " + issueId));
    }

    private Issue dtoToEntity(UserDTO currentUser, Long issueId, IssueDto issueDto) {
        Issue issue;
        
        if (issueId != null) {
            issue = findIssueByIdOrThrow(issueId);
        } else {
            issue = new Issue();
        }
        
        // Map basic fields
        issue.setTitle(issueDto.getTitle());
        issue.setDescription(issueDto.getDescription());
        issue.setTags(issueDto.getTags());
        issue.setDueDate(issueDto.getDueDate());
        issue.setNotes(issueDto.getNotes());
        issue.setEstimatedHours(issueDto.getEstimatedHours());
        issue.setActualHours(issueDto.getActualHours());
        
        // Map enums
        if (issueDto.getStatus() != null) {
            issue.setStatus(IssueStatus.valueOf(issueDto.getStatus()));
        }
        if (issueDto.getPriority() != null) {
            issue.setPriority(IssuePriority.valueOf(issueDto.getPriority()));
        }
        if (issueDto.getIssueType() != null) {
            issue.setIssueType(IssueType.valueOf(issueDto.getIssueType()));
        }
        
        // Map relationships
        if (issueDto.getAssigneeId() != null) {
            User assignee = userRepository1.findById(issueDto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
            issue.setAssignee(assignee);
        }
        
        if (issueDto.getReporterId() != null) {
            User reporter = userRepository1.findById(issueDto.getReporterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reporter not found"));
            issue.setReporter(reporter);
        }
        
        return issue;
    }

    private IssueDto entityToDto(Issue issue) {
        IssueDto.IssueDtoBuilder builder = IssueDto.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus().name())
                .priority(issue.getPriority().name())
                .issueType(issue.getIssueType().name())
                .tags(issue.getTags())
                .dueDate(issue.getDueDate())
                .resolvedDate(issue.getResolvedDate())
                .acknowledgedDate(issue.getAcknowledgedDate())
                .notes(issue.getNotes())
                .estimatedHours(issue.getEstimatedHours())
                .actualHours(issue.getActualHours())
                .createdAt(issue.getCreatedDate() != null ? issue.getCreatedDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null)
                .lastModifiedDate(issue.getLastModifiedDate() != null ? issue.getLastModifiedDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null)
                .createdBy(issue.getCreatedBy())
                .lastModifiedBy(issue.getLastModifiedBy());

        if (issue.getAssignee() != null) {
            builder.assigneeId(issue.getAssignee().getId())
                   .assignee(io.getarrays.securecapita.dto.UserDTO.toDto(issue.getAssignee()));
        }

        if (issue.getReporter() != null) {
            builder.reporterId(issue.getReporter().getId())
                   .reporter(io.getarrays.securecapita.dto.UserDTO.toDto(issue.getReporter()));
        }

        if (issue.getProject() != null) {
            builder.projectId(issue.getProject().getId())
                   .projectName(issue.getProject().getName());
        }

        return builder.build();
    }

    private IssueAcknowledgmentDto acknowledgmentToDto(IssueAcknowledgment acknowledgment) {
        return IssueAcknowledgmentDto.builder()
                .id(acknowledgment.getId())
                .issueId(acknowledgment.getIssue().getId())
                .issueTitle(acknowledgment.getIssue().getTitle())
                .acknowledgedBy(io.getarrays.securecapita.dto.UserDTO.toDto(acknowledgment.getAcknowledgedBy()))
                .acknowledgmentDate(acknowledgment.getAcknowledgmentDate())
                .notes(acknowledgment.getNotes())
                .signature(acknowledgment.getSignature())
                .signatureType(acknowledgment.getSignatureType())
                .signatureTimestamp(acknowledgment.getSignatureTimestamp())
                .ipAddress(acknowledgment.getIpAddress())
                .userAgent(acknowledgment.getUserAgent())
                .certificateInfo(acknowledgment.getCertificateInfo())
                .signatureHash(acknowledgment.getSignatureHash())
                .build();
    }
}
