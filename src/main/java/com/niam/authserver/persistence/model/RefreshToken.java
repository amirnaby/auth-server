package com.niam.authserver.persistence.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@SequenceGenerator(name = "refresh_token_sequence", sequenceName = "refresh_token_sequence", allocationSize = 1)
@Data
@Entity(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_sequence")
    private long id;

    @OneToOne
    @JoinColumn
    private User user;

    @Column(nullable = false, unique = true, columnDefinition = "text", length = 10485760)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;
}