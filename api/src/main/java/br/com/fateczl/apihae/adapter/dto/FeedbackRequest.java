package br.com.fateczl.apihae.adapter.dto;

import br.com.fateczl.apihae.domain.enums.FeedbackCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
    private String name;
    private String email;
    private String subject;
    private FeedbackCategory category;
    private String description;
}
