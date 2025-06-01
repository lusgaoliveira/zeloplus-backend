package br.com.zelo.puls.zeloplus.mapper;

import br.com.zelo.puls.zeloplus.dto.PegarListaTarefaDTO;
import br.com.zelo.puls.zeloplus.model.Tarefa;

import java.util.List;
import java.util.stream.Collectors;

public class TarefaMapper {
    public static PegarListaTarefaDTO toDTO(Tarefa tarefa) {
        return new PegarListaTarefaDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getStatus().name()
        );
    }

    public static List<PegarListaTarefaDTO> toDTOList(List<Tarefa> tarefas) {
        return tarefas.stream()
                .map(TarefaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
