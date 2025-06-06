package br.com.zelo.puls.zeloplus.dto;

import java.time.LocalDateTime;

public record PegarListaTarefaDTO(
        Integer id,
        String titulo,
        String statusTarefa,
        LocalDateTime dataAgendamento
){
}
