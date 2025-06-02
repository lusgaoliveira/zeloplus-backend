package br.com.zelo.puls.zeloplus.dto;

import br.com.zelo.puls.zeloplus.model.StatusTarefa;
import br.com.zelo.puls.zeloplus.model.TipoTarefa;

import java.time.LocalDateTime;

public record PegarTarefaDTO(
        Integer id,
        String titulo,
        String descricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAgendamento,
        TipoTarefa tipoTarefa,
        int nivel,
        StatusTarefa statusTarefa

) {
}
