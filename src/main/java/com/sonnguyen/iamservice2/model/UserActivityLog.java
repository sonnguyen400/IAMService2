package com.sonnguyen.iamservice2.model;

import com.sonnguyen.iamservice2.constant.ActivityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserActivityLog extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;
    private String ipAddress;
    private String userAgent;
    private String email;
}
