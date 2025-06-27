package io.getarrays.securecapita.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor
@Table(name = "`task`")
public class Task extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    private String title;

    private String description;

    private Date initiatedDate;

    private Date startDate;

    private Date dueDate;

    @Column
    @Enumerated(EnumType.STRING)
    @Nonnull
    private PriorityEnum priority;

    @Column
    @Enumerated(EnumType.STRING)
    @Nonnull
    private TaskStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "initiated_user_id", nullable = false)
    private User initiatedUser;

    @ManyToMany
    @JoinTable(name = "task_assigned_users", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns =
    @JoinColumn(name = "user_id"))
    private Set<User> assignedUsers = new HashSet<>();


}