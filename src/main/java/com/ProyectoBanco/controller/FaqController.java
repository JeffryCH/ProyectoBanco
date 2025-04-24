package com.ProyectoBanco.controller;

import com.ProyectoBanco.domain.Faq;
import com.ProyectoBanco.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FaqController {
    @Autowired
    private FaqService faqService;

    @GetMapping("/faq/faq")
    public String mostrarFaqs(Model model) {
        List<Faq> faqs = faqService.listarFaqs();
        model.addAttribute("faqs", faqs);
        return "faq/faq";
    }
}
