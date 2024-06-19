package com.github.helpdesk.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.helpdesk.domain.Cliente;
import com.github.helpdesk.domain.dto.ClienteDto;
import com.github.helpdesk.service.ClienteService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Cliente")
public class ClienteResource {

	private ClienteService service;
	
	public ClienteResource(ClienteService service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteDto> findById(@PathVariable Integer id) {
		Cliente Cliente = service.findById(id);
		return ResponseEntity.ok(new ClienteDto(Cliente));
	}

	@GetMapping
	public ResponseEntity<List<ClienteDto>> findAll() {
		List<Cliente> list = service.findAll();
		List<ClienteDto> listDto = list.stream().map(Cliente -> new ClienteDto(Cliente)).collect(Collectors.toList());
		return ResponseEntity.ok(listDto);
	}

	@PostMapping
	public ResponseEntity<ClienteDto> create(@Valid @RequestBody ClienteDto dto) {
		Cliente Cliente = service.create(dto);
		ClienteDto ClienteToDto = new ClienteDto(Cliente);
		return new ResponseEntity<>(ClienteToDto, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteDto> update(@PathVariable Integer id, @Valid @RequestBody ClienteDto dto) {
		Cliente Cliente = service.update(id, dto);
		return ResponseEntity.ok().body(new ClienteDto(Cliente));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ClienteDto> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
