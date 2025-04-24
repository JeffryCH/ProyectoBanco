package com.ProyectoBanco.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "FAQ")
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PREGUNTA")
    private Long id;

    @Column(name = "PREGUNTA", nullable = false, length = 255)
    private String pregunta;

    @Column(name = "RESPUESTA", nullable = false, length = 1000)
    private String respuesta;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPregunta() { return pregunta; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }
    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
}
