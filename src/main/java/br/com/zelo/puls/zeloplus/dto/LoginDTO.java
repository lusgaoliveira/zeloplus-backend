package br.com.zelo.puls.zeloplus.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank
        String nomeUsuario,

        @NotBlank
        String senha
) {
}
