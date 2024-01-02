package com.jst.domain.post.repository;

import com.jst.common.entity.BaseEntity;
import com.jst.domain.member.repository.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="post")
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column
    private String title;

    @Column
    private String contents;

    //@OneToMany를 통해 Order와 join된다. Member하나는 여러개의 Order를 가질수 있다. 반대
    // 여래개의 게시글은 하나의 카테고리만 가질 수 있어서 ManyToOne
    @ManyToOne
    @JoinColumn(name="category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name="created_by")
    private UserEntity createdBy;

    @ManyToOne
    @JoinColumn(name="update_by")
    private UserEntity updateBy;
}
