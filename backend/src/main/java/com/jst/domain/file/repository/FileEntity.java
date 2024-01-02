package com.jst.domain.file.repository;

import jakarta.persistence.*;

@Entity
@Table(name="file")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private Long size;

    @Column
    private Long extension;

    @Column(name = "file_group_id")
    private Long fileGroupId;
}
