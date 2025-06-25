package com.co2fp.backend.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="Transport")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Transport_Id")
    private Long transport_id;
    @Column(name="TransportName")
    private String transport_name;
    @Column(name="Fuel_Factor")
    private Integer fuel_factor;
    @Column(name="Emission_Factor")
    private Integer emission_factor;
    @OneToMany(mappedBy = "transport", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerEntity> answer;
}
