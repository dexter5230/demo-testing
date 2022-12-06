package com.demo.testpractice.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Administrator")
@Table(name = "administrator", uniqueConstraints = @UniqueConstraint( name = "unique_username", columnNames = "admin_username"))
public class Administrator {
    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "uuid_generator", strategy = "uuid4")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(name = "admin_id", columnDefinition = "CHAR(36)")
    private UUID adminId;

    @Column (name = "admin_username", columnDefinition = "TEXT")
    private String userName;

    @Column (name = "password", columnDefinition = "TEXT")
    @JsonIgnoreProperties
    private String password;

    public Administrator(UUID adminId, String userName, String password) {
        this.adminId = adminId;
        this.userName = userName;
        this.password = password;
    }

    public Administrator() {
    }

    public Administrator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
