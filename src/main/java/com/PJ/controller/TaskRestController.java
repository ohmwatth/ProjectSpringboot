package com.PJ.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PJ.dtos.TaskDTO;
import com.PJ.model.Subject;
import com.PJ.model.Task;
import com.PJ.model.User;
import com.PJ.repository.SubjectRepository;
import com.PJ.repository.TaskRepository;
import com.PJ.repository.UserRepository;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;
    private final SubjectRepository subjectRepo;

    public TaskRestController(TaskRepository taskRepo, UserRepository userRepo, SubjectRepository subjectRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.subjectRepo = subjectRepo;
    }

    
    @GetMapping
    public List<Task> getTasks(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUsername(userDetails.getUsername())
                            .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepo.findByStudent(user);
    }

   
    @PostMapping("/add")
    public Task addTask(@AuthenticationPrincipal UserDetails userDetails,
                        @RequestBody TaskDTO req) {

        User student = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();

        Subject subject;

        if (req.getSubjectId() != null) {
            subject = subjectRepo.findById(req.getSubjectId())
                                 .orElseThrow(() -> new RuntimeException("Subject not found"));
        } else if (req.getSubjectName() != null && !req.getSubjectName().isEmpty()) {
            List<Subject> subjects = subjectRepo.findByUserAndName(student, req.getSubjectName());
            if (!subjects.isEmpty()) {
                subject = subjects.get(0);
            } else {
                subject = new Subject();
                subject.setName(req.getSubjectName());
                subject.setTitle(req.getSubjectTitle() != null ? req.getSubjectTitle() : req.getSubjectName());
                subject.setUser(student);
                subject.setCode("N/A");
                subjectRepo.save(subject);
            }
        } else {
            throw new RuntimeException("กรุณาเลือกหรือพิมพ์วิชา");
        }

        Task task = new Task();
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setFinished(false);
        task.setStudent(student);
        task.setSubject(subject);

        return taskRepo.save(task);
    }

   
    @PutMapping("/toggle-status/{id}")
    public Task toggleStatus(@PathVariable Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setFinished(!task.isFinished());
        return taskRepo.save(task);
    }

    
    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    
    @PutMapping("/edit/{id}")
    public Task editTask(@PathVariable Long id, @RequestBody TaskDTO req) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        Subject subject = task.getSubject();

        subject.setName(req.getSubjectName());
        subject.setTitle(req.getSubjectTitle() != null ? req.getSubjectTitle() : req.getSubjectName());
        subjectRepo.save(subject);

        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        return taskRepo.save(task);
    }

   
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepo.deleteById(id);
        return "Task deleted successfully";
    }
}
