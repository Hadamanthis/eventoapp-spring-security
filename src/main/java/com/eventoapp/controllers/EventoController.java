package com.eventoapp.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.repository.EventoRepository;

@Controller
public class EventoController {

	@Autowired
	private EventoRepository repository;
	
	@Autowired
	private ConvidadoRepository convidadoRepository;
	
	@RequestMapping(value = "evento", method = RequestMethod.GET)
	public String form() {
		return "evento/evento-form";
	}
	
	@RequestMapping(value = "evento", method = RequestMethod.POST)
	public String save(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Todos os campos são obrigatórios");
			return "redirect:evento";
		}
		
		repository.save(evento);
		
		attributes.addFlashAttribute("mensagem", "Evento salvo com sucesso!");
		return "redirect:/evento";
	}
	
	@RequestMapping(value = "list-eventos")
	public ModelAndView findAll() {
		ModelAndView mv = new ModelAndView("index");
		
		Iterable<Evento> eventos = this.repository.findAll();
		
		mv.addObject("eventos", eventos);
		
		return mv;
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
	public ModelAndView findById(@PathVariable Long codigo) {
		ModelAndView mv = new ModelAndView("evento/detalhes-evento");
		
		Evento evento = repository.findByCodigo(codigo);
		
		mv.addObject("evento", evento);
		
		return mv;
	}
	
	@RequestMapping(value = "delete")
	public String deleteEvento(Long codigo) {
		
		Evento evento = repository.findByCodigo(codigo);
		
		repository.delete(evento);
		
		return "redirect:/list-eventos";
	}
	
	@RequestMapping(value = "delete-convidado")
	public String deleteConvidado(String rg) {
		
		Convidado convidado = convidadoRepository.findByRg(rg);
		
		convidadoRepository.delete(convidado);
		
		return "redirect:" + convidado.getEvento().getCodigo();
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.POST)
	public String saveConvidado(@PathVariable Long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Todos os campos são obrigatórios");
			return "redirect:/{codigo}";
		}
		
		Evento evento = repository.findByCodigo(codigo);
		convidado.setEvento(evento);
		
		convidadoRepository.save(convidado);
		
		attributes.addFlashAttribute("mensagem", "Convidado salvo com sucesso!");
		return "redirect:/{codigo}";
	}
}
