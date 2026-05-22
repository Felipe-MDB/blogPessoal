package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

import jakarta.validation.Valid;

// Anotações: alterar e/ou definir comportamentos

@RestController 				// Indica que a classe é uma controller (recebe requisições e responde)
@RequestMapping("/postagens")  // Indica que as requisições do endpoint / "postagens" serão tratada pela classe controller
@CrossOrigin(origins = "*", allowedHeaders = "*")	// é uma ferramenta de segurança, ele indica que qualquer front interaja com a nossa classe controladora
													// ela permite que a controller receba requisições
public class PostagemController {
	
	@Autowired	// ele que aplica a inversão de controle, "inversão de dependência", quem era dependente agora é autonomo. autoriza a quebrar a regra de POO
	private PostagemRepository postagemRepository;
	
	// cria a classe repository | implementa o método da Interface | instancia um objeto da classe repository
	
	@GetMapping("/{id}")	// GET/POST/PUT/DELETE
	public ResponseEntity<Postagem> getById(@PathVariable Long id){		// (responseEntity) entidade de resposta, ela monta uma caixinha de resposta, retorno informações, se deu certo, se deu errado.
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	@PostMapping	
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
		
		postagem.setId(null);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(postagemRepository.save(postagem));
	}
	
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
		return postagemRepository.findById(postagem.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.OK)
					.body(postagemRepository.save(postagem)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
				
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Postagem> postagem = postagemRepository.findById(id);
		
		if(postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		postagemRepository.deleteById(id);
	}
	
}
