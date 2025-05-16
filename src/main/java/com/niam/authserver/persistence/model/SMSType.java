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
@Entity(name = "sms_type")
@SequenceGenerator(name = "sms_type_sequence", sequenceName = "sms_type_sequence", allocationSize = 1)
public class SMSType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sms_type_sequence")
    private Long id;
    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL)
    private ServiceType serviceType;
    @Column(nullable = false)
    private String name;
}