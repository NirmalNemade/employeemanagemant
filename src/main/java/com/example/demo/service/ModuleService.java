package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.entity.Module;
import com.example.demo.enums.Role;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.enums.ContentType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final ModuleCompletionRepository completionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ModuleCompletionRepository moduleCompletionRepository;
    

    private static final Logger logger = LoggerFactory.getLogger(ModuleService.class);

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            logger.error("No authenticated user found in security context");
            throw new RuntimeException("No authenticated user found");
        }

        String email = authentication.getName();
        logger.info("Fetching user with email: {}", email);

        return userRepository.findByEmail(email);

    }

    public ModuleResponseDto getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found with ID: " + id));

        System.out.println("Module found: " + module); // Debugging line

        return mapToResponseDto(module);
    }

    private ModuleResponseDto mapToResponseDto(Module module) {
        System.out.println("Mapping module: " + module); // Debugging line

        return new ModuleResponseDto(
                module.getId(),
                module.getTitle(),
                module.getCourse() != null ? module.getCourse().getName() : null, // Avoid NullPointerException
                module.getDescription(),
                module.getContentType(),
                module.getContentUrl(),
                module.isPublished(),
                module.getAddedBy() != null ? module.getAddedBy().getId() : null);
    }

    @Transactional
public void deleteModule(Long id) {
    Module module = moduleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Module not found"));

    // Explicitly delete related module_completion records
    moduleCompletionRepository.deleteByModuleId(id);

    // Now delete the module
    moduleRepository.delete(module);
}

    // Instructor: Create a new module
    @Transactional
    public ModuleResponseDto createModule(ModuleRequestDto request) {
        User instructor = getCurrentUser();
        if (instructor.getRole() != (Role.INSTRUCTOR)) {
            throw new RuntimeException("Only instructors can create modules");
        }

        Course course = courseRepository.findByInstructorIdAndName(instructor.getId(), request.getCourseName())
                .orElseGet(() -> {
                    Course newCourse = new Course();
                    newCourse.setName(request.getCourseName());
                    newCourse.setInstructor(instructor);
                    return courseRepository.save(newCourse);
                });

        Module module = new Module();
        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        module.setContentType(request.getContentType());
        module.setContentUrl(request.getContentUrl());
        module.setCourse(course);
        module.setAddedBy(instructor);
        module = moduleRepository.save(module);

        return mapToResponseDto(module);
    }

    // Instructor: Update a module
    @Transactional
    public ModuleResponseDto updateModule(Long moduleId, ModuleRequestDto request) {
        User instructor = getCurrentUser();
        if (instructor.getRole() != (Role.INSTRUCTOR)) {
            throw new RuntimeException("Only instructors can update modules");
        }

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        if (!module.getCourse().getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("You can only update your own modules");
        }

        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        module.setContentUrl(request.getContentUrl());
        module = moduleRepository.save(module);

        return mapToResponseDto(module);
    }

    // Instructor: Publish/Unpublish a module
    @Transactional
    public ModuleResponseDto togglePublishModule(Long moduleId, boolean publish) {
        User instructor = getCurrentUser();
        if (instructor.getRole() != (Role.INSTRUCTOR)) {
            throw new RuntimeException("Only instructors can publish/unpublish modules");
        }

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        if (!module.getCourse().getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("You can only modify your own modules");
        }

        module.setPublished(publish);
        module = moduleRepository.save(module);

        return mapToResponseDto(module);
    }

    // Instructor: Get all my modules
    public List<ModuleResponseDto> getMyModules() {
        User instructor = getCurrentUser();
        if (instructor.getRole() != Role.INSTRUCTOR) {
            throw new RuntimeException("Only instructors can view their modules");
        }

        // Fetch modules directly where the course's instructor matches the current user
        List<Module> modules = moduleRepository.findByAddedById(instructor.getId());

        return modules.stream()
                .map(this::mapToResponseDtoWithModelMapper)
                .collect(Collectors.toList());
    }

    public List<ModuleResponseDto> browseModules(String courseName, String title, ContentType contentType) {
        User student = getCurrentUser();
        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Only students can browse modules");
        }

        List<Module> modules = new ArrayList<>();

        if (courseName != null) {
            modules.addAll(moduleRepository.findByCourseNameContainingIgnoreCaseAndPublishedTrue(courseName));
        }
        if (title != null) {
            modules.addAll(moduleRepository.findByTitleContainingIgnoreCaseAndPublishedTrue(title));
        }
        if (contentType != null) {
            modules.addAll(moduleRepository.findByContentTypeAndPublishedTrue(contentType));
        }

        // If no filters are applied, return all published modules
        if (modules.isEmpty()) {
            modules = moduleRepository.findByPublishedTrue();
        }

        return modules.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Student: Mark module as completed
    @Transactional
    public void markModuleCompleted(Long moduleId) {
        User student = getCurrentUser();
        // Check if the user has the STUDENT role
        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Only students can mark modules as completed");
        }

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        if (!module.isPublished()) {
            throw new RuntimeException("Cannot complete an unpublished module");
        }

        ModuleCompletion completion = completionRepository.findByStudentIdAndModuleId(student.getId(), moduleId)
                .orElseGet(() -> {
                    ModuleCompletion newCompletion = new ModuleCompletion();
                    newCompletion.setStudent(student);
                    newCompletion.setModule(module);
                    return newCompletion;
                });
        completion.setCompleted(true);
        completionRepository.save(completion);
    }

    public ProgressDto getProgress() {
        User student = getCurrentUser();
        System.out.println("Current User ID: " + student.getId());

        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Only students can view progress");
        }

        List<ModuleCompletion> completions = completionRepository.findByStudentId(student.getId());
        int completedModules = (int) completions.stream().filter(ModuleCompletion::isCompleted).count();
        if (completions.isEmpty()) {
            System.out.println("No module completions found for Student ID: " + student.getId());
        } else {
            System.out.println("Found " + completions.size() + " completions.");
        }
        int totalModules = (int) moduleRepository.countByPublishedTrue(); // More efficient query

        ProgressDto progress = new ProgressDto();
        progress.setTotalModules(totalModules);
        progress.setCompletedModules(completedModules);
        progress.setProgressPercentage(totalModules > 0 ? (completedModules * 100.0 / totalModules) : 0);
        return progress;
    }

    // Instructor: Get completion report
    public List<CompletionReportDto> getCompletionReport() {
        User instructor = getCurrentUser();
        System.out.println("Instructor ID: " + instructor.getId());

        if (instructor.getRole() != Role.INSTRUCTOR) {
            throw new RuntimeException("Only instructors can view completion reports");
        }

        // Fetch all modules where the instructor owns the course
        List<Module> modules = moduleRepository.findByAddedById(instructor.getId());
        System.out.println("Total modules found: " + modules.size());

        if (modules.isEmpty()) {
            return Collections.emptyList();
        }

        long totalStudents = userRepository.countByRole(Role.STUDENT);
        System.out.println("Total students: " + totalStudents);

        return modules.stream().map(module -> {
            System.out.println("Fetching completions for Module ID: " + module.getId());

            List<ModuleCompletion> completions = completionRepository.findByModuleId(module.getId());
            int completedCount = (int) completions.stream().filter(ModuleCompletion::isCompleted).count();

            System.out.println("Completed count for module " + module.getTitle() + ": " + completedCount);

            CompletionReportDto report = new CompletionReportDto();
            report.setModuleId(module.getId());
            report.setModuleTitle(module.getTitle());
            report.setCourseName(module.getCourse().getName());
            report.setCompletedCount(completedCount);
            report.setCompletionPercentage(totalStudents > 0 ? (completedCount * 100.0 / totalStudents) : 0);

            return report;
        }).collect(Collectors.toList());
    }

    private ModuleResponseDto mapToResponseDtoWithModelMapper(Module module) {
        ModuleResponseDto dto = modelMapper.map(module, ModuleResponseDto.class);
        dto.setCourseName(module.getCourse().getName());
        return dto;
    }
}