package br.com.zelo.puls.zeloplus.dto;


import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CriarTarefaDTO(

        String titulo,
        String descricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAgendamento,
        @NotNull(message = "Id não pode ser nulo")
        Integer idTipoTarefa,
        Integer id,
        int nivel) {
}
