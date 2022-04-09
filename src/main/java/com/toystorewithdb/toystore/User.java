package com.toystorewithdb.toystore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @NotBlank(message = "Please provide your username", groups = {LoginChecks.class, SignupChecks.class})
    @Size(min=4,
          message = "Username must have more than 3 characters",
          groups = {LoginChecks.class, SignupChecks.class})
    private String username;

    @NotBlank(message = "Please provide your full name", groups = SignupChecks.class)
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$",
             message = "Username must contain alphabetic and whitespace characters only",
             groups = SignupChecks.class)
    private String fullname;

    @NotBlank(message = "Please provide your email address", groups = SignupChecks.class)
    @Email(message = "Must be a valid email address", groups = SignupChecks.class)
    private String email;

    @NotBlank(message = "Please provide your password", groups = {LoginChecks.class, SignupChecks.class})
    @Pattern(regexp = ".*(([A-Z]+.*[^a-zA-Z0-9 ]+)|([^a-zA-Z0-9 ]+.*[A-Z]+)).*",
             message = "Must contain both capital letter and non-alphanumeric characters (%$#@! etc.)",
             groups = {LoginChecks.class, SignupChecks.class})
    @Size(min = 9, message = "Must be more than 8 characters", groups = {LoginChecks.class, SignupChecks.class})
    private String password;
    
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String name) {
        this.username = name;
    }
    
    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String name) {
        this.fullname = name;
    }
    
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }
    
}
