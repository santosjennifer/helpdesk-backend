package com.github.helpdesk.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.helpdesk.domain.Pessoa;
import com.github.helpdesk.domain.Tecnico;
import com.github.helpdesk.domain.dto.TecnicoDto;
import com.github.helpdesk.repository.PessoaRepository;
import com.github.helpdesk.repository.TecnicoRepository;
import com.github.helpdesk.service.exception.DataIntegrityViolationException;
import com.github.helpdesk.service.exception.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class TecnicoService {

	private TecnicoRepository repository;
	private PessoaRepository pessoaRepository;
	private PasswordEncoder encoder;
	
	public TecnicoService(TecnicoRepository repository, PessoaRepository pessoaRepository, PasswordEncoder encoder) {
		this.repository = repository;
		this.pessoaRepository = pessoaRepository;
		this.encoder = encoder;
	}
	
	public Tecnico findById(Integer id) {
		Optional<Tecnico> tecnico = repository.findById(id);
		return tecnico.orElseThrow(() 
				-> new ObjectNotFoundException("Técnico não encontrado. ID: " + id));
	}

	public List<Tecnico> findAll() {
		return repository.findAll();
	}

	public Tecnico create(TecnicoDto dto) {
		dto.setId(null);
		dto.setSenha(encoder.encode(dto.getSenha()));
		validaPorCpfEEmail(dto);
		Tecnico tecnico = new Tecnico(dto);
		return repository.save(tecnico);
	}
	
	public Tecnico update(Integer id, @Valid TecnicoDto dto) {
		dto.setId(id);
		Tecnico tecnico = findById(id);
		
		if (!dto.getSenha().equals(tecnico.getSenha())) {
			dto.setSenha(encoder.encode(dto.getSenha()));
		}
		
		validaPorCpfEEmail(dto);
		tecnico = new Tecnico(dto);
		return repository.save(tecnico);
	}
	
	public void delete(Integer id) {
		Tecnico tecnico = findById(id);
		if (tecnico.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Técnico possui ordem de serviço e não pode ser excluido");
		}
		repository.deleteById(id);
	}

	private void validaPorCpfEEmail(TecnicoDto dto) {
		Optional<Pessoa> pessoa = pessoaRepository.findByCpf(dto.getCpf());
		if (pessoa.isPresent() && pessoa.get().getId() != dto.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado");
		}
		
		pessoa = pessoaRepository.findByEmail(dto.getEmail());
		if (pessoa.isPresent() && pessoa.get().getId() != dto.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado");
		}
	}
	
}
