package com.github.helpdesk.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.helpdesk.domain.Tecnico;
import com.github.helpdesk.domain.dto.TecnicoDto;
import com.github.helpdesk.service.TecnicoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tecnicos")
@Tag(name = "Tecnico")
public class TecnicoResource {

	private TecnicoService service;
	
	public TecnicoResource(TecnicoService service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public ResponseEntity<TecnicoDto> findById(@PathVariable Integer id) {
		Tecnico tecnico = service.findById(id);
		return ResponseEntity.ok(new TecnicoDto(tecnico));
	}

	@GetMapping
	public ResponseEntity<List<TecnicoDto>> findAll() {
		List<Tecnico> list = service.findAll();
		List<TecnicoDto> listDto = list.stream().map(tecnico 
				-> new TecnicoDto(tecnico)).collect(Collectors.toList());
		return ResponseEntity.ok(listDto);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<TecnicoDto> create(@Valid @RequestBody TecnicoDto dto) {
		Tecnico tecnico = service.create(dto);
		TecnicoDto tecnicoToDto = new TecnicoDto(tecnico);
		return new ResponseEntity<>(tecnicoToDto, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<TecnicoDto> update(@PathVariable Integer id, @Valid @RequestBody TecnicoDto dto) {
		Tecnico tecnico = service.update(id, dto);
		return ResponseEntity.ok().body(new TecnicoDto(tecnico));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<TecnicoDto> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
