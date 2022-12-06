package com.demo.testpractice.admin;

public class AdminRegistrationRequest {
    private Administrator admin;

    public AdminRegistrationRequest(Administrator admin) {
        this.admin = admin;
    }

    public AdminRegistrationRequest() {
    }

    public Administrator getAdmin() {
        return admin;
    }
}
