package com.github.helpdesk.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.helpdesk.domain.Cliente;
import com.github.helpdesk.domain.Pessoa;
import com.github.helpdesk.domain.dto.ClienteDto;
import com.github.helpdesk.repository.ClienteRepository;
import com.github.helpdesk.repository.PessoaRepository;
import com.github.helpdesk.service.exception.DataIntegrityViolationException;
import com.github.helpdesk.service.exception.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class ClienteService {

	private ClienteRepository repository;
	private PessoaRepository pessoaRepository;
	private PasswordEncoder encoder;
	
	public ClienteService(ClienteRepository repository, PessoaRepository pessoaRepository, PasswordEncoder encoder) {
		this.repository = repository;
		this.pessoaRepository = pessoaRepository;
		this.encoder = encoder;
	}
	
	public Cliente findById(Integer id) {
		Optional<Cliente> cliente = repository.findById(id);
		return cliente.orElseThrow(() 
				-> new ObjectNotFoundException("Cliente não encontrado. ID: " + id));
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public Cliente create(ClienteDto dto) {
		dto.setId(null);
		dto.setSenha(encoder.encode(dto.getSenha()));
		validaPorCpfEEmail(dto);
		Cliente cliente = new Cliente(dto);
		return repository.save(cliente);
	}
	
	public Cliente update(Integer id, @Valid ClienteDto dto) {
		dto.setId(id);
		Cliente cliente = findById(id);
		
		if (!dto.getSenha().equals(cliente.getSenha())) {
			dto.setSenha(encoder.encode(dto.getSenha()));
		}
		
		validaPorCpfEEmail(dto);
		cliente = new Cliente(dto);
		return repository.save(cliente);
	}
	
	public void delete(Integer id) {
		Cliente cliente = findById(id);
		if (cliente.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Cliente possui ordem de serviço e não pode ser excluido.");
		}
		repository.deleteById(id);
	}

	private void validaPorCpfEEmail(ClienteDto dto) {
		Optional<Pessoa> pessoa = pessoaRepository.findByCpf(dto.getCpf());
		if (pessoa.isPresent() && pessoa.get().getId() != dto.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado.");
		}
		
		pessoa = pessoaRepository.findByEmail(dto.getEmail());
		if (pessoa.isPresent() && pessoa.get().getId() != dto.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado.");
		}
	}
	
}
