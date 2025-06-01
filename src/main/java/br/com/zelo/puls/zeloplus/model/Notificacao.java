package br.com.zelo.puls.zeloplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titulo;
    private String conteudo;
    private LocalDateTime dataEnvio;

    @Enumerated(EnumType.STRING)
    private TipoNotificacao tipo;

    @ManyToOne
    @JoinColumn(name = "idoso_id")
    private Idoso idoso;

    @ManyToOne
    @JoinColumn(name = "cuidador_id")
    private Cuidador cuidador;
}
