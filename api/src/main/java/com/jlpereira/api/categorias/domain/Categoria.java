package com.jlpereira.api.categorias.domain;

import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.id.uuid.UuidVersion7Strategy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @UuidGenerator(algorithm = UuidVersion7Strategy.class)
    private UUID id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            ;
        if (!(obj instanceof Categoria another))
            return false;
        return id != null && id.equals(another.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
