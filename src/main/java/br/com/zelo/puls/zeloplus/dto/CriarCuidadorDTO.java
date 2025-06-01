package br.com.zelo.puls.zeloplus.dto;

import java.time.LocalDate;

public record CriarCuidadorDTO(
        String nome,
        LocalDate dataNascimento,
        String codigoVinculo
) {
}
