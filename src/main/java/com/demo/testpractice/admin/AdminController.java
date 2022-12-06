package com.demo.testpractice.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "api/adminReg/v1")
public class AdminController {
    // registration service invoked
    private final AdminService adminService;
    private final CustomerManagementService customerManagementService;

    @Autowired
    public AdminController(AdminService adminService, CustomerManagementService customerManagementService) {
        this.adminService = adminService;
        this.customerManagementService = customerManagementService;

    }

    @PutMapping
    private void adminRegistration(@RequestBody AdminRegistrationRequest adminRegistrationRequest) {
        adminService.registerCustomer(adminRegistrationRequest);
    }

    @DeleteMapping
    private void customerRemove (@RequestBody CustomerRemoveRequest customerRemoveRequest) {
        customerManagementService.removeCustomer(customerRemoveRequest);
    }

}
