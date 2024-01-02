package com.jst.domain.member.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jst.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="member")
public class UserEntity extends BaseEntity {
    @Id
    @Column
    private String id;

    @Column(name = "user_name")
    private String userNm;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column
    private String phone;

}
