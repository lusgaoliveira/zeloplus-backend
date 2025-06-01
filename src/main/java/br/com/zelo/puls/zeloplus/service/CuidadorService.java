package br.com.zelo.puls.zeloplus.service;

import br.com.zelo.puls.zeloplus.dto.CriarCuidadorDTO;
import br.com.zelo.puls.zeloplus.model.Cuidador;
import br.com.zelo.puls.zeloplus.model.Idoso;
import br.com.zelo.puls.zeloplus.model.Usuario;
import br.com.zelo.puls.zeloplus.repository.CuidadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CuidadorService {
    private final CuidadorRepository cuidadorRepository;

    public CuidadorService(CuidadorRepository cuidadorRepository) {
        this.cuidadorRepository = cuidadorRepository;
    }

    public void salvar(CriarCuidadorDTO dto, Idoso idoso, Usuario usuario) {

        Cuidador cuidador = new Cuidador(
                null,
                dto.nome(),
                dto.dataNascimento(),
                idoso,
                usuario,
                dto.codigoVinculo()
        );
        cuidadorRepository.save(cuidador);
    }

    public Cuidador buscarPorId(Integer codigo) {
        return cuidadorRepository.findById(codigo).orElseThrow(
                () -> new IllegalArgumentException(("Cuidador não encontrado!"))
        );
    }
    public List<Cuidador> listar() {
        return cuidadorRepository.findAll();
    }


}
