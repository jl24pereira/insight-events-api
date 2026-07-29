package com.jlpereira.api.historial.domain;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import com.jlpereira.api.eventos.domain.Evento;
import com.jlpereira.api.historial.domain.enums.AccionHistorial;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.id.uuid.UuidVersion7Strategy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "historial_evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialEvento {

    @Id
    @UuidGenerator(algorithm = UuidVersion7Strategy.class)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "usuario", nullable = false, length = 150)
    private String usuario;

    @Column(name = "fecha", nullable = false)
    private OffsetDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "accion", nullable = false, length = 30)
    private AccionHistorial accion;

    @Column(name = "comentario", columnDefinition = "text")
    private String comentario;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HistorialEvento otro))
            return false;
        return id != null && id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
