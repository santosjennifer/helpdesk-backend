package com.github.helpdesk.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.helpdesk.domain.Chamado;
import com.github.helpdesk.domain.dto.ChamadoDto;
import com.github.helpdesk.service.ChamadoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/chamados")
@Tag(name = "Chamado")
public class ChamadoResource {

	private ChamadoService service;
	
	public ChamadoResource(ChamadoService service) {
		this.service = service;
	}

	@GetMapping("{id}")
	public ResponseEntity<ChamadoDto> findById(@PathVariable Integer id) {
		Chamado chamado = service.findById(id);
		return ResponseEntity.ok(new ChamadoDto(chamado));
	}

	@GetMapping
	public ResponseEntity<List<ChamadoDto>> findAll() {
		List<Chamado> list = service.findAll();
		List<ChamadoDto> listDto = list.stream().map(chamado -> new ChamadoDto(chamado)).collect(Collectors.toList());
		return ResponseEntity.ok(listDto);
	}

	@PostMapping
	public ResponseEntity<ChamadoDto> create(@Valid @RequestBody ChamadoDto dto) {
		Chamado chamado = service.create(dto);
		return new ResponseEntity<>(new ChamadoDto(chamado), HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	public ResponseEntity<ChamadoDto> update(@PathVariable Integer id, @Valid @RequestBody ChamadoDto dto) {
		Chamado chamado = service.update(id, dto);
		return ResponseEntity.ok(new ChamadoDto(chamado));
	}

}
