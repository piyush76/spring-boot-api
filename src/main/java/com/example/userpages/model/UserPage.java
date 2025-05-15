package com.example.userpages.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPage {
    private String companyId;
    private Long personnelId;
    private String pageId;
    private String adminRole;
}
