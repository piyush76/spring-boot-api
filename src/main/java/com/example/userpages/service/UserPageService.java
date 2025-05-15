package com.example.userpages.service;

import com.example.userpages.model.UserPage;
import com.example.userpages.repository.UserPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPageService {

    private final UserPageRepository userPageRepository;

    public UserPageService(UserPageRepository userPageRepository) {
        this.userPageRepository = userPageRepository;
    }

    public List<UserPage> getUserPages(Long userId, String companyId) {
        return userPageRepository.findByUserIdAndCompanyId(userId, companyId);
    }

    public Optional<UserPage> getUserPage(Long userId, String companyId, String pageId) {
        return userPageRepository.findByUserIdCompanyIdAndPageId(userId, companyId, pageId);
    }

    public void saveUserPage(UserPage userPage) {
        userPageRepository.save(userPage);
    }

    public void deleteUserPage(Long userId, String companyId, String pageId) {
        userPageRepository.delete(userId, companyId, pageId);
    }

    public void deleteAllUserPages(Long userId, String companyId) {
        userPageRepository.deleteAll(userId, companyId);
    }
}
