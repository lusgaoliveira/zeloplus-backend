package br.com.zelo.puls.zeloplus.dto;

import java.time.LocalDate;

public record CriarIdosoDTO(
        String nome,
        LocalDate dataNascimento
) {
}
