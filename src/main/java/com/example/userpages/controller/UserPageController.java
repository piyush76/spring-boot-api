package com.example.userpages.controller;

import com.example.userpages.model.UserPage;
import com.example.userpages.service.UserPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/companies/{companyId}/pages")
@Tag(name = "User Pages", description = "API for managing user pages")
public class UserPageController {

    private final UserPageService userPageService;

    public UserPageController(UserPageService userPageService) {
        this.userPageService = userPageService;
    }

    @GetMapping
    @Operation(summary = "Get all pages for a user in a company", 
               description = "Retrieves all pages associated with a specific user in a specific company")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pages",
                     content = @Content(schema = @Schema(implementation = UserPage.class))),
        @ApiResponse(responseCode = "404", description = "User or company not found")
    })
    public ResponseEntity<List<UserPage>> getUserPages(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Company ID") @PathVariable String companyId) {
        
        List<UserPage> userPages = userPageService.getUserPages(userId, companyId);
        
        if (userPages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(userPages);
    }

    @GetMapping("/{pageId}")
    @Operation(summary = "Get a specific page for a user in a company", 
               description = "Retrieves a specific page associated with a user in a company")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the page",
                     content = @Content(schema = @Schema(implementation = UserPage.class))),
        @ApiResponse(responseCode = "404", description = "Page, user, or company not found")
    })
    public ResponseEntity<UserPage> getUserPage(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Company ID") @PathVariable String companyId,
            @Parameter(description = "Page ID") @PathVariable String pageId) {
        
        return userPageService.getUserPage(userId, companyId, pageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{pageId}")
    @Operation(summary = "Create or update a page for a user in a company", 
               description = "Creates a new page or updates an existing page for a user in a company")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Page successfully updated"),
        @ApiResponse(responseCode = "201", description = "Page successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> saveUserPage(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Company ID") @PathVariable String companyId,
            @Parameter(description = "Page ID") @PathVariable String pageId,
            @RequestBody UserPage userPage) {
        
        if (!userPage.getPersonnelId().equals(userId) || 
            !userPage.getCompanyId().equals(companyId) || 
            !userPage.getPageId().equals(pageId)) {
            return ResponseEntity.badRequest().build();
        }
        
        boolean exists = userPageService.getUserPage(userId, companyId, pageId).isPresent();
        
        userPageService.saveUserPage(userPage);
        
        return exists ? 
                ResponseEntity.ok().build() : 
                ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{pageId}")
    @Operation(summary = "Delete a specific page for a user in a company", 
               description = "Deletes a specific page associated with a user in a company")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Page successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Page, user, or company not found")
    })
    public ResponseEntity<Void> deleteUserPage(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Company ID") @PathVariable String companyId,
            @Parameter(description = "Page ID") @PathVariable String pageId) {
        
        if (!userPageService.getUserPage(userId, companyId, pageId).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        userPageService.deleteUserPage(userId, companyId, pageId);
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all pages for a user in a company", 
               description = "Deletes all pages associated with a user in a company")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pages successfully deleted")
    })
    public ResponseEntity<Void> deleteAllUserPages(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Company ID") @PathVariable String companyId) {
        
        userPageService.deleteAllUserPages(userId, companyId);
        
        return ResponseEntity.noContent().build();
    }
}
