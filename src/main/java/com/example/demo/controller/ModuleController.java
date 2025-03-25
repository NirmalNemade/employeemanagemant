package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.enums.ContentType;
import com.example.demo.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import com.example.demo.entity.Module;

@RestController
@RequestMapping("/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    // Instructor: Create a module
    @PostMapping
    public ResponseEntity<ModuleResponseDto> createModule(@RequestBody ModuleRequestDto request) {
        return new ResponseEntity<>(moduleService.createModule(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getModuleById(@PathVariable Long id) {
        ModuleResponseDto module = moduleService.getModuleById(id);
        if (module != null) {
            return ResponseEntity.ok(module);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModule(@PathVariable Long id) {
        try {
            moduleService.deleteModule(id);
            return ResponseEntity.ok("Module deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Instructor: Update a module
    @PutMapping("/{moduleId}")
    public ResponseEntity<ModuleResponseDto> updateModule(@PathVariable Long moduleId,
            @RequestBody ModuleRequestDto request) {
        return ResponseEntity.ok(moduleService.updateModule(moduleId, request));
    }

    // Instructor: Publish/Unpublish a module
    @PatchMapping("/{moduleId}/publish")
    public ResponseEntity<ModuleResponseDto> togglePublishModule(@PathVariable Long moduleId,
            @RequestParam boolean publish) {
        return ResponseEntity.ok(moduleService.togglePublishModule(moduleId, publish));
    }

    // Instructor: Get my modules
    @GetMapping("/my-modules")
    public ResponseEntity<List<ModuleResponseDto>> getMyModules() {
        return ResponseEntity.ok(moduleService.getMyModules());
    }

    // Student: Browse modules
    @GetMapping("/modules/browse")
    public ResponseEntity<List<ModuleResponseDto>> browseModules(
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) ContentType contentType) {
        return ResponseEntity.ok(moduleService.browseModules(courseName, title, contentType));
    }

    // Student: Mark module as completed
    @PostMapping("/{moduleId}/complete")
    public ResponseEntity<Void> markModuleCompleted(@PathVariable Long moduleId) {
        moduleService.markModuleCompleted(moduleId);
        return ResponseEntity.ok().build();
    }

    // Student: Get progress
    @GetMapping("/progress")
    public ResponseEntity<ProgressDto> getProgress() {
        return ResponseEntity.ok(moduleService.getProgress());
    }

    // Instructor: Get completion report
    @GetMapping("/report")
    public ResponseEntity<List<CompletionReportDto>> getCompletionReport() {
        return ResponseEntity.ok(moduleService.getCompletionReport());
    }
}