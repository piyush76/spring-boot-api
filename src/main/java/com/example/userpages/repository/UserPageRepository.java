package com.example.userpages.repository;

import com.example.userpages.model.UserPage;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserPageRepository {

    private final DSLContext dslContext;

    public UserPageRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<UserPage> findByUserIdAndCompanyId(Long userId, String companyId) {
        Result<Record> result = dslContext.select()
                .from(DSL.table("CUSTOMER_USER_PAGE"))
                .where(DSL.field("PERSONNEL_ID").eq(userId).and(DSL.field("COMPANY_ID").eq(companyId)))
                .fetch();

        return result.stream()
                .map(this::mapToUserPage)
                .collect(Collectors.toList());
    }

    public Optional<UserPage> findByUserIdCompanyIdAndPageId(Long userId, String companyId, String pageId) {
        Record record = dslContext.select()
                .from(DSL.table("CUSTOMER_USER_PAGE"))
                .where(DSL.field("PERSONNEL_ID").eq(userId)
                        .and(DSL.field("COMPANY_ID").eq(companyId))
                        .and(DSL.field("PAGE_ID").eq(pageId)))
                .fetchOne();

        return Optional.ofNullable(record).map(this::mapToUserPage);
    }

    public void save(UserPage userPage) {
        boolean exists = findByUserIdCompanyIdAndPageId(
                userPage.getPersonnelId(), 
                userPage.getCompanyId(), 
                userPage.getPageId()
        ).isPresent();
        
        if (exists) {
            dslContext.update(DSL.table("CUSTOMER_USER_PAGE"))
                    .set(DSL.field("ADMIN_ROLE"), userPage.getAdminRole())
                    .where(DSL.field("COMPANY_ID").eq(userPage.getCompanyId())
                            .and(DSL.field("PERSONNEL_ID").eq(userPage.getPersonnelId()))
                            .and(DSL.field("PAGE_ID").eq(userPage.getPageId())))
                    .execute();
        } else {
            dslContext.insertInto(
                            DSL.table("CUSTOMER_USER_PAGE"),
                            DSL.field("COMPANY_ID"),
                            DSL.field("PERSONNEL_ID"),
                            DSL.field("PAGE_ID"),
                            DSL.field("ADMIN_ROLE")
                    )
                    .values(
                            userPage.getCompanyId(),
                            userPage.getPersonnelId(),
                            userPage.getPageId(),
                            userPage.getAdminRole()
                    )
                    .execute();
        }
    }

    public void delete(Long userId, String companyId, String pageId) {
        dslContext.deleteFrom(DSL.table("CUSTOMER_USER_PAGE"))
                .where(DSL.field("PERSONNEL_ID").eq(userId)
                        .and(DSL.field("COMPANY_ID").eq(companyId))
                        .and(DSL.field("PAGE_ID").eq(pageId)))
                .execute();
    }

    public void deleteAll(Long userId, String companyId) {
        dslContext.deleteFrom(DSL.table("CUSTOMER_USER_PAGE"))
                .where(DSL.field("PERSONNEL_ID").eq(userId)
                        .and(DSL.field("COMPANY_ID").eq(companyId)))
                .execute();
    }

    private UserPage mapToUserPage(Record record) {
        return UserPage.builder()
                .companyId(record.get("COMPANY_ID", String.class))
                .personnelId(record.get("PERSONNEL_ID", Long.class))
                .pageId(record.get("PAGE_ID", String.class))
                .adminRole(record.get("ADMIN_ROLE", String.class))
                .build();
    }
}
