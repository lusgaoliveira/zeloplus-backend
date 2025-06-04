package br.com.zelo.puls.zeloplus.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PerfilDTO(
        Integer id,
        String nomeUsuario,
        String fotoPerfil,
        String email,
        String nome,
        LocalDate dataNascimento,
        @NotBlank
        String tipoUsuario,
        Integer idUsuario,
        String codigoVinculo
) {
}
