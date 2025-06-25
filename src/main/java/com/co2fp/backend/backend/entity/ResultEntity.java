package com.co2fp.backend.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Result")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Result_Id")
    private Integer result_Id;
    @Column(name="CoPerDay")
    private Integer coperday;
    @Column(name="CoPerMonth")
    private Integer copermonth;
    @Column(name="CoPerYear")
    private Integer coperyear;
}
