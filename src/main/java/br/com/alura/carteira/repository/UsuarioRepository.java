package br.com.alura.carteira.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.carteira.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public Optional<Usuario> findByLogin(String login);

	@Query("select u from Usuario u join fetch u.perfis where u.id = :id")
	public Optional<Usuario> carregarPorIdComPerfis(Long id);

	public Boolean existsByLogin(String login);

}
