package com.PJ.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // stored encoded

    private String email;
    private String fullName;
    private String tel; // ✅ เปลี่ยนเป็นตัวเล็กเพื่อให้ getter/setter ถูกต้อง
    private String role = "STUDENT"; // default เป็นนักเรียน

    // One-to-many: งาน (Task/Checklist) ของนักเรียน
    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private Set<Task> tasks;
}
