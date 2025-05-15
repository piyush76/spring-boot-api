package com.example.userpages.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCompany {
    private String companyId;
    private Long personnelId;
    private String status;
    private String defaultFacilityId;
    private String defaultHub;
    private String adminRole;
    private String dataSource;
    private Date lockoutDate;
    private String defaultOpsEntityId;
    private String itarRole;
    private Character msdsOcRole;
    private String userPreferenceData;
}
