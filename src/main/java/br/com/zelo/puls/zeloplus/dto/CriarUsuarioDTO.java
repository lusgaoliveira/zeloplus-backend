package br.com.zelo.puls.zeloplus.dto;

import java.time.LocalDate;

public record CriarUsuarioDTO(
        String nomeUsuario,
        String senha,
        String email,
        String nome,
        String tipoUsuario,
        LocalDate dataNascimento,
        String codigoVinculo,
        String tokenExpo
) {
}
