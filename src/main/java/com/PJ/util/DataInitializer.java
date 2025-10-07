package com.PJ.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.PJ.model.Task;
import com.PJ.model.User;


import com.PJ.repository.TaskRepository;
import com.PJ.repository.UserRepository;


@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final TaskRepository taskRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepo, TaskRepository taskRepo,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepo.findByUsername("student1").isEmpty()) {
            User student = new User();
            student.setUsername("student1");
            student.setPassword(passwordEncoder.encode("student1"));
            student.setRole("STUDENT");
            student.setFullName("นักเรียน 1");
            userRepo.save(student);

            Task t1 = new Task();
            t1.setTitle("อ่านบทที่ 1");
            t1.setStudent(student);

            Task t2 = new Task();
            t2.setTitle("ทำแบบฝึกหัดหน้า 5-6");
            t2.setStudent(student);

            taskRepo.save(t1);
            taskRepo.save(t2);
        }

        if (userRepo.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setRole("ADMIN");
            admin.setFullName("ผู้ดูแลระบบ");
            userRepo.save(admin);
        }
    }
}