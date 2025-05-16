package com.niam.authserver.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Entity(name = "service_type")
@SequenceGenerator(name = "service_type_sequence", sequenceName = "service_type_sequence", allocationSize = 1)
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_type_sequence")
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
}