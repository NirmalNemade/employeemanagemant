package com.example.demo.repository;

import com.example.demo.entity.ModuleCompletion;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ModuleCompletionRepository extends JpaRepository<ModuleCompletion, Long> {
    Optional<ModuleCompletion> findByStudentIdAndModuleId(Long studentId, Long moduleId);

    List<ModuleCompletion> findByStudentId(Long studentId);

    List<ModuleCompletion> findByModuleId(Long moduleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ModuleCompletion mc WHERE mc.module.id = :moduleId")
    void deleteByModuleId(@Param("moduleId") Long moduleId);

}