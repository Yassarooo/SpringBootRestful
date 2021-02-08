package Project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.sun.istack.NotNull;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;

/**
 * Model class for application user
 *
 * @author
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "username",unique = true)
    private String username;
    @NotNull
    @Email
    @Column(name = "email",unique = true)
    private String email;
    @Column(name = "gender")
    private String gender;
    @Column(name = "phonenumber")
    private String phonenumber;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    @Column(name = "dob")
    private Date dob;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "roles",
            joinColumns = {
                    @JoinColumn(name = "userid")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "roleid")})
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
