package com.github.helpdesk.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.helpdesk.domain.Chamado;
import com.github.helpdesk.domain.Cliente;
import com.github.helpdesk.domain.Tecnico;
import com.github.helpdesk.domain.dto.ChamadoDto;
import com.github.helpdesk.domain.enums.Prioridade;
import com.github.helpdesk.domain.enums.Status;
import com.github.helpdesk.repository.ChamadoRepository;
import com.github.helpdesk.service.exception.ObjectNotFoundException;

@Service
public class ChamadoService {
	
	private ChamadoRepository repository;
	private TecnicoService tecnicoService;
	private ClienteService clienteService;
	
	public ChamadoService(ChamadoRepository repository, TecnicoService tecnicoService, ClienteService clienteService) {
		this.repository = repository;
		this.tecnicoService = tecnicoService;
		this.clienteService = clienteService;
	}
	
	public Chamado findById(Integer id) {
		Optional<Chamado> chamado = repository.findById(id);
		return chamado.orElseThrow(() -> new ObjectNotFoundException("Chamado não encontrado. ID: " + id));
	}
	
	public List<Chamado> findAll(){
		return repository.findAll();
	}

	public Chamado create(ChamadoDto dto) {
		return repository.save(newChamado(dto));
	}
	
	public Chamado update(Integer id, ChamadoDto dto) {
		Chamado chamado = findById(id);
		chamado = newChamado(dto);
		return repository.save(chamado);
	}
	
	private Chamado newChamado(ChamadoDto dto) {
		Tecnico tecnico = tecnicoService.findById(dto.getTecnico());
		Cliente cliente = clienteService.findById(dto.getCliente());
		
		Chamado chamado = new Chamado();
		if (dto.getId() != null) {
			chamado.setId(dto.getId());
		}
		
		if (dto.getStatus().equals(2)) {
			chamado.setDataFechamento(LocalDate.now());
		}
		
		chamado.setTecnico(tecnico);
		chamado.setCliente(cliente);
		chamado.setPrioridade(Prioridade.toEnum(dto.getPrioridade()));
		chamado.setStatus(Status.toEnum(dto.getStatus()));
		chamado.setTitulo(dto.getTitulo());
		chamado.setObservacoes(dto.getObservacoes());
		return chamado;
	}

}
