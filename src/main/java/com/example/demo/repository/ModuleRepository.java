package com.example.demo.repository;

import com.example.demo.entity.Module;
import com.example.demo.enums.ContentType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    // List<Module> findByCourseId(Long courseId);

    List<Module> findByCourseIdAndPublishedTrue(Long courseId);

    List<Module> findByCourseNameContainingIgnoreCaseAndPublishedTrue(String courseName);

    List<Module> findByTitleContainingIgnoreCaseAndPublishedTrue(String title);

    List<Module> findByContentTypeAndPublishedTrue(ContentType contentType);

    List<Module> findByAddedById(Long instructorId);

    List<Module> findByPublishedTrue();

    @Query("SELECT COUNT(m) FROM Module m WHERE m.published = true")
    int countByPublishedTrue();

    @Query("SELECT m FROM Module m WHERE m.course.id = :courseId")
    List<Module> findByCourseId(@Param("courseId") Long courseId);

}