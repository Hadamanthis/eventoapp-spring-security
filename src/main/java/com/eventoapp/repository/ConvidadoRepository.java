package com.eventoapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.eventoapp.models.Convidado;

public interface ConvidadoRepository extends CrudRepository<Convidado, Long> {
	
	public Convidado findByRg(String rg);
	
}
