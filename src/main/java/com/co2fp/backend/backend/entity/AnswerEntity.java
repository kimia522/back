package com.co2fp.backend.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="Answer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Answer_Id")
    private Long answer_id;
    @Column(name="DATE")
    private LocalDate date;
    @Column(name="TIME")
    private LocalTime time;
    @Column(name="DISTANCE")
    private Long  distance;
    @Column(name="Passenger_Count")
    private Long  passenger_count;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="AnswerByTransport",referencedColumnName = "Transport_Id",nullable = true)
    private TransportEntity transport;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="AnswerByUser", referencedColumnName = "User_Id",nullable = true)
    private UserEntity user;
}
