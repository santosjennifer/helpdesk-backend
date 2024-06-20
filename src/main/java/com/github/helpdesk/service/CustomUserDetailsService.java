package com.github.helpdesk.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.helpdesk.domain.Pessoa;
import com.github.helpdesk.repository.PessoaRepository;
import com.github.helpdesk.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private PessoaRepository repository;

	public CustomUserDetailsService(PessoaRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Pessoa> user = repository.findByEmail(email);
		if (user.isPresent()) {
			return new CustomUserDetails(user.get().getId(), user.get().getEmail(), user.get().getSenha(), user.get().getPerfis());
		}
		throw new UsernameNotFoundException(email);
	}

}
