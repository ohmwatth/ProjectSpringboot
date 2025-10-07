package com.PJ.controller;
import com.PJ.model.Subject;
import com.PJ.model.User;
import com.PJ.repository.SubjectRepository;
import com.PJ.repository.UserRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectRestController {

    private final SubjectRepository subjectRepo;
    private final UserRepository userRepo;

    public SubjectRestController(SubjectRepository subjectRepo, UserRepository userRepo) {
        this.subjectRepo = subjectRepo;
        this.userRepo = userRepo;
    }

   
    @GetMapping
    public List<Subject> listSubjects(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        return subjectRepo.findByUser(user);
    }

    
    @PostMapping("/add")
    public Subject addSubject(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestBody Subject subject) {
        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        subject.setUser(user);
        return subjectRepo.save(subject);
    }

    
    @DeleteMapping("/{id}")
    public String deleteSubject(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        Subject subject = subjectRepo.findById(id).orElseThrow();

        if (!subject.getUser().equals(user)) {
            return "You are not authorized to delete this subject";
        }

        subjectRepo.delete(subject);
        return "Subject deleted successfully";
    }
}
