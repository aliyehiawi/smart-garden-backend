package org.smartgarden.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Garden garden;

    @Column(nullable = false, length = 255)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean read;
}


