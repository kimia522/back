package com.co2fp.backend.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="User_Id")
    private Long user_id;
    @Column(name="UserName",unique = true)
    private String username;
    @Column(name="PassWord")
    private String password;
    @Column(name="EmaiL",unique = true)
    private String email;
    @Column(name="FirstName")
    private String firstname;
    @Column(name="LastName")
    private String lastname;
    @Column(name="Admin",nullable = false)
    private Boolean admin=false;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerEntity> answer;
}
