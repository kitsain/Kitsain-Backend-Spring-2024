package com.myblogbackend.blog.models;

import com.myblogbackend.blog.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Boolean approved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="category_id" )
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id" )
    private UserEntity user;
    public void approved() {
        this.approved = false;
    }
}
