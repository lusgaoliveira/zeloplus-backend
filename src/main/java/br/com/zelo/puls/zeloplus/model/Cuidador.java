package br.com.zelo.puls.zeloplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cuidadores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cuidador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private LocalDate dataNascimento;
    @OneToOne
    @JoinColumn(name = "idoso_id")
    private Idoso idoso;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String codigoVinculo;

}
