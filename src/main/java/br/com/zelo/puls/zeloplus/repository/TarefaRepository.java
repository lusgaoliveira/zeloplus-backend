package br.com.zelo.puls.zeloplus.repository;

import br.com.zelo.puls.zeloplus.model.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {
    Page<Tarefa> findByIdosoId(Integer idIdoso, Pageable pageable);
}
