package com.PJ.controller;


import com.PJ.model.Subject;
import com.PJ.model.Task;
import com.PJ.model.User;
import com.PJ.repository.SubjectRepository;
import com.PJ.repository.TaskRepository;
import com.PJ.repository.UserRepository;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TaskController {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;
    private final SubjectRepository subjectRepo;

    public TaskController(TaskRepository taskRepo, UserRepository userRepo,SubjectRepository subjectRepo){
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.subjectRepo = subjectRepo;
    }

    @GetMapping("/tasks")
    public String showTasks(@AuthenticationPrincipal UserDetails userDetails, Model model) {
  
        User user = userRepo.findByUsername(userDetails.getUsername())
                            .orElseThrow(() -> new RuntimeException("User not found"));


        model.addAttribute("username", user.getFullName()); 


        List<Task> tasks = taskRepo.findByStudent(user);

  
        Map<String, List<Task>> groupedTasks = tasks.stream()
                .collect(Collectors.groupingBy(t -> t.getSubject().getName().substring(0, 1).toUpperCase()));

        model.addAttribute("groupedTasks", groupedTasks);

        return "task";
    }

    @PostMapping("/tasks/add")
    public String addTask(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam String title,
                          @RequestParam(required = false) Long subjectId,
                          @RequestParam(required = false) String subjectName,
                          @RequestParam(required = false) String subjectTitle,
                          @RequestParam(required = false) String iconUrl) {

        User student = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();

        Task task = new Task();
        task.setTitle(title);
        task.setStudent(student);
        task.setFinished(false);
        Subject subject;

        if (subjectId != null) {
  
            subject = subjectRepo.findById(subjectId)
                                 .orElseThrow(() -> new RuntimeException("Subject not found"));
        } else if (subjectName != null && !subjectName.isEmpty()) {
      
        	List<Subject> subjects = subjectRepo.findByUserAndName(student, subjectName);
        	if (!subjects.isEmpty()) {
        	    subject = subjects.get(0); 
        	} else {
        	    subject = new Subject();
        	    subject.setName(subjectName);
        	    subject.setTitle(subjectTitle != null ? subjectTitle : subjectName);
        	    subject.setUser(student);
        	    subject.setCode("N/A");
        	    subjectRepo.save(subject);
        	}
        } else {
            throw new RuntimeException("กรุณาเลือกหรือพิมพ์วิชา");
        }

        task.setSubject(subject);
        taskRepo.save(task);

        return "redirect:/tasks";
    }
    @PostMapping("/tasks/toggle-status/{id}")
    public String toggleStatus(@PathVariable Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setFinished(!task.isFinished());
        taskRepo.save(task);
        return "redirect:/tasks";
    }
    @GetMapping("/tasks/view/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        Task task = taskRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        model.addAttribute("task", task);
        return "view-task"; 
    }
    
    @GetMapping("/tasks/edit/{id}")
    public String editTaskPage(@PathVariable Long id, Model model) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        model.addAttribute("task", task);
        return "edit_task";
    }

    @PostMapping("/tasks/edit/{id}")
    public String editTask(@PathVariable Long id,
                           @RequestParam String title,
                           @RequestParam String description,
                           @RequestParam String subjectName,
                           @RequestParam String subjectTitle,
                           @RequestParam(required = false) String iconUrl) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        Subject subject = task.getSubject();


        subject.setName(subjectName);
        subject.setTitle(subjectTitle != null ? subjectTitle : subjectName);
        subjectRepo.save(subject);

     
        task.setTitle(title);
        task.setDescription(description);
        taskRepo.save(task);

        return "redirect:/tasks";
    }
    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id){
        taskRepo.deleteById(id);
        return "redirect:/tasks";
    }
}