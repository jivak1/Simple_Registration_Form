package org.example.model;


import lombok.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(name = "username", nullable = false, unique = false)
    private String username ;

    @Column(name = "password", nullable = false, unique = false)
    private String password ;

    @Column(name = "email", nullable = false, unique = true)
    private String email ;

    @Column(name = "active", nullable = false, unique = false)
    private Boolean isActive ;

    @Column(name = "verificationToken", nullable = false, unique = false)
    private String verificationToken ;

    @Column(name = "sessionVerificationToken", unique = true)
    private String sessionVerificationToken ;

    public UserEntity(String username, String password, String email, String verificationToken)
    {
        this.username = username ;
        this.password = password ;
        this.email = email ;
        this.isActive = false ;
        this.verificationToken = verificationToken ;
    }

}
