package br.com.zelo.puls.zeloplus.repository;

import br.com.zelo.puls.zeloplus.model.Cuidador;
import br.com.zelo.puls.zeloplus.model.Idoso;
import br.com.zelo.puls.zeloplus.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdosoRepository extends JpaRepository<Idoso, Integer> {
    Optional<Idoso> findByCodigoVinculo(String codigoVinculo);
    Optional<Idoso> findByUsuario(Usuario usuario);
}
