package br.com.fateczl.apihae.adapter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
    private String name;
    private String email;
    private String feedback;
}
