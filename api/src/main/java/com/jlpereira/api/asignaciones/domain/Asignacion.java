package com.jlpereira.api.asignaciones.domain;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import com.jlpereira.api.analistas.domain.Analista;
import com.jlpereira.api.asignaciones.domain.enums.EstadoAsignacion;
import com.jlpereira.api.eventos.domain.Evento;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.id.uuid.UuidVersion7Strategy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asignacion")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asignacion {

    @Id
    @UuidGenerator(algorithm = UuidVersion7Strategy.class)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "analista_id", nullable = false)
    private Analista analista;

    @Column(name = "fecha_asignacion", nullable = false)
    private OffsetDateTime fechaAsignacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoAsignacion estado;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Asignacion otra))
            return false;
        return id != null && id.equals(otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
