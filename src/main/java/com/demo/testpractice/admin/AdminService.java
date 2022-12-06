package com.demo.testpractice.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AdminService {
    private AdminRepo adminRepo;
    @Autowired
    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }
    @Transactional
    @Modifying
    public void registerCustomer(AdminRegistrationRequest adminRegistrationRequest) {
        if (adminRegistrationRequest.getAdmin().getAdminId() == null) {
            adminRegistrationRequest.getAdmin().setAdminId(UUID.randomUUID());
        }
        if (adminRegistrationRequest.getAdmin() == null) {
            throw new IllegalStateException("The admin is null");
        }
        boolean isExist = adminRepo.findById(adminRegistrationRequest.getAdmin().getAdminId()).isPresent();
        if (isExist) {
            throw new IllegalStateException("The admin is already exist");
        } else {
            adminRepo.save(adminRegistrationRequest.getAdmin());
        }
    }
}
