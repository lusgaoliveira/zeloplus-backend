package br.com.zelo.puls.zeloplus.dto;

import java.time.LocalDate;

public record PerfilUpdateDTO(
        String fotoPerfil,
        String email,
        String nome,
        LocalDate dataNascimento
) {
}
