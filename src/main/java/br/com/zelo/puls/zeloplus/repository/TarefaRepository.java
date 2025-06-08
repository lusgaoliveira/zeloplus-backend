package br.com.zelo.puls.zeloplus.repository;

import br.com.zelo.puls.zeloplus.model.StatusTarefa;
import br.com.zelo.puls.zeloplus.model.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {
    Page<Tarefa> findByIdosoIdAndStatusNot(Integer idIdoso, StatusTarefa status, Pageable pageable);

    // Pega por status
    @Query("""
        SELECT t.status, COUNT(t) 
        FROM Tarefa t 
        WHERE t.idoso.id = :idosoId 
        AND (:mes IS NULL OR EXTRACT(MONTH FROM t.dataAgendamento) = :mes) 
        GROUP BY t.status
    """)
    List<Object[]> contarTarefasPorStatus(Integer idosoId, Integer mes);

    // Pega por nível
    @Query("""
        SELECT t.nivel, COUNT(t) 
        FROM Tarefa t 
        WHERE t.idoso.id = :idosoId 
        AND (:mes IS NULL OR EXTRACT(MONTH FROM t.dataAgendamento) = :mes) 
        GROUP BY t.nivel
    """)
    List<Object[]> contarTarefasPorNivel(Integer idosoId, Integer mes);


    // Pega por tipo tarefa
    @Query("""
        SELECT t.tipo.nome, COUNT(t) 
        FROM Tarefa t 
        WHERE t.idoso.id = :idosoId 
        AND (:mes IS NULL OR EXTRACT(MONTH FROM t.dataAgendamento) = :mes) 
        GROUP BY t.tipo.nome
    """)
    List<Object[]> contarTarefasPorTipo(Integer idosoId, Integer mes);
}
