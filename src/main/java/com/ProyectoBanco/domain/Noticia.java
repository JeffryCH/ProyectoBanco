package com.ProyectoBanco.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NOTICIAS")
public class Noticia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NOTICIA")
    private Long id;

    @Column(name = "TITULO", nullable = false, length = 255)
    private String titulo;

    @Column(name = "DESCRIPCION", nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "FECHA", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Column(name = "IMAGEN_URL", length = 1000)
    private String imagenUrl;

    // Constructor para noticias con imagen
    public Noticia(String titulo, java.util.Date fecha, String descripcion, String imagenUrl) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }
    public Noticia() {}

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}
