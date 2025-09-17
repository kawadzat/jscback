package io.getarrays.securecapita.ProjectManagement;

import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/issue")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping
    public ResponseEntity<CustomMessage> createIssue(@AuthenticationPrincipal UserDTO currentUser,
                                                    @RequestBody @Valid IssueDto issueDto) {
        return ResponseEntity.ok(new CustomMessage("Issue Created Successfully", 
                issueService.createIssue(currentUser, issueDto)));
    }

    @PutMapping("/{issueId}")
    public ResponseEntity<CustomMessage> updateIssue(@AuthenticationPrincipal UserDTO currentUser,
                                                    @PathVariable Long issueId,
                                                    @RequestBody @Valid IssueDto issueDto) {
        return ResponseEntity.ok(new CustomMessage("Issue Updated Successfully", 
                issueService.updateIssue(currentUser, issueId, issueDto)));
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<IssueDto> getIssueById(@PathVariable Long issueId) {
        return ResponseEntity.ok(issueService.getIssueById(issueId));
    }

    @GetMapping
    public ResponseEntity<List<IssueDto>> getAllIssues() {
        return ResponseEntity.ok(issueService.getAllIssues());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IssueDto>> getIssuesByStatus(@PathVariable String status) {
        IssueStatus issueStatus = IssueStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(issueService.getIssuesByStatus(issueStatus));
    }

    @PutMapping("/{issueId}/status")
    public ResponseEntity<CustomMessage> changeIssueStatus(@AuthenticationPrincipal UserDTO currentUser,
                                                          @PathVariable Long issueId,
                                                          @RequestBody String status) {
        IssueStatus issueStatus = IssueStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(new CustomMessage("Issue Status Updated Successfully", 
                issueService.changeIssueStatus(currentUser, issueId, issueStatus)));
    }

    @PutMapping("/{issueId}/assign")
    public ResponseEntity<CustomMessage> assignIssue(@AuthenticationPrincipal UserDTO currentUser,
                                                    @PathVariable Long issueId,
                                                    @RequestBody AssignIssueRequest assignRequest) {
        return ResponseEntity.ok(new CustomMessage("Issue Assigned Successfully", 
                issueService.assignIssue(currentUser, issueId, assignRequest.getAssigneeId())));
    }

    @PostMapping("/{issueId}/acknowledge")
    public ResponseEntity<CustomMessage> acknowledgeIssue(@AuthenticationPrincipal UserDTO currentUser,
                                                         @PathVariable Long issueId,
                                                         @RequestBody IssueAcknowledgmentDto acknowledgmentDto) {
        return ResponseEntity.ok(new CustomMessage("Issue Acknowledged Successfully", 
                issueService.acknowledgeIssue(currentUser, issueId, acknowledgmentDto)));
    }

    @PostMapping("/{issueId}/manual-acknowledge")
    public ResponseEntity<CustomMessage> manuallyAcknowledgeIssue(@AuthenticationPrincipal UserDTO currentUser,
                                                                 @PathVariable Long issueId,
                                                                 @RequestBody(required = false) String notes) {
        return ResponseEntity.ok(new CustomMessage("Issue Manually Acknowledged and Status Changed to ACKNOWLEDGED", 
                issueService.manuallyAcknowledgeIssue(currentUser, issueId, notes)));
    }

    @GetMapping("/pending-acknowledgment")
    public ResponseEntity<List<IssueDto>> getIssuesPendingAcknowledgment() {
        return ResponseEntity.ok(issueService.getIssuesPendingAcknowledgment());
    }

    @GetMapping("/pending-acknowledgment/assignee/{assigneeId}")
    public ResponseEntity<List<IssueDto>> getIssuesPendingAcknowledgmentByAssignee(@PathVariable Long assigneeId) {
        return ResponseEntity.ok(issueService.getIssuesPendingAcknowledgmentByAssignee(assigneeId));
    }

    @GetMapping("/{issueId}/acknowledgment")
    public ResponseEntity<IssueAcknowledgmentDto> getIssueAcknowledgment(@PathVariable Long issueId) {
        IssueAcknowledgmentDto acknowledgment = issueService.getIssueAcknowledgment(issueId);
        if (acknowledgment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(acknowledgment);
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<CustomMessage> deleteIssue(@PathVariable Long issueId) {
        issueService.deleteIssue(issueId);
        return ResponseEntity.ok(new CustomMessage("Issue Deleted Successfully", null));
    }

    // Inner class for assign request
    public static class AssignIssueRequest {
        private Long assigneeId;

        public Long getAssigneeId() {
            return assigneeId;
        }

        public void setAssigneeId(Long assigneeId) {
            this.assigneeId = assigneeId;
        }
    }
}



