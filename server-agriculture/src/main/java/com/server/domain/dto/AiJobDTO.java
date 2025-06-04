package com.server.domain.dto;

import com.server.domain.AgricultureJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiJobDTO {
    private String species;
    private ArrayList<AgricultureJob> jobs;
}
