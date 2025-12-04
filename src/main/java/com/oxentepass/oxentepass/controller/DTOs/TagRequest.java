package com.oxentepass.oxentepass.controller.DTOs;

import com.oxentepass.oxentepass.entity.Tag;

import jakarta.validation.constraints.NotBlank;

public record CriarTagDTO(
    @NotBlank(message = "A nova tag deve ter um nome.")
    String tag
) {
    public Tag paraEntidade() {
        Tag tag = new Tag();
        tag.setTag(this.tag);
        return tag;
    }
}
