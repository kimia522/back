package com.co2fp.backend.backend.DTO.transport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportRegisterDTO {
    private String transport_name;
    private Integer fuel_factor;
    private Integer emission_factor;
}
