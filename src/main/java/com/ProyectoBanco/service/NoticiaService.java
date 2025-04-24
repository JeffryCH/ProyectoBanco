package com.ProyectoBanco.service;

import com.ProyectoBanco.domain.Noticia;
import com.ProyectoBanco.repository.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticiaService {
    @Autowired
    private NoticiaRepository noticiaRepository;

    public List<Noticia> listarNoticias() {
        return noticiaRepository.findAll();
    }
    
    public void guardar(Noticia noticia) {
        noticiaRepository.save(noticia);
    }
    
    public void eliminar(Long id) {
        noticiaRepository.deleteById(id);
    }
    
    public Noticia obtenerPorId(Long id) {
        return noticiaRepository.findById(id).orElse(null);
    }
}
