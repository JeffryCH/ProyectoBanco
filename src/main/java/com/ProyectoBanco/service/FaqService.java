package com.ProyectoBanco.service;

import com.ProyectoBanco.domain.Faq;
import com.ProyectoBanco.repository.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqService {
    @Autowired
    private FaqRepository faqRepository;

    public List<Faq> listarFaqs() {
        return faqRepository.findAll();
    }
    
    public void guardar(Faq faq) {
        faqRepository.save(faq);
    }
    
    public void eliminar(Long id) {
        faqRepository.deleteById(id);
    }
    
    public Faq obtenerPorId(Long id) {
        return faqRepository.findById(id).orElse(null);
    }
}
