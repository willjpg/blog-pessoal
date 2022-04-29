package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

	@Autowired
	private PostagemRepository postagemRepository;
	
	@GetMapping
	public ResponseEntity <List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll());
		
		// select * from tb_postagens;
	}
	
	//buscar uma id
	@GetMapping("/{id}")
	public ResponseEntity <Postagem> getById(@PathVariable Long id){
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))//função lambda
				.orElse(ResponseEntity.notFound().build());
		
		// select * from tb_postagens where id = id
	}
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity <List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
		
		// select * from tb_postagens where titulo like "%titulo%";
		
	}
	
	@PostMapping			//Created 201.status	//save. salva ou atualiza
	public ResponseEntity <Postagem> postPostagem(@Valid @RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
		
	}
	
	@PutMapping		//Put para atualizar		//CREATED vira OK				//save. salva ou atualiza
	public ResponseEntity <Postagem> putPostagem(@Valid @RequestBody Postagem postagem){//confirmar que nâo há id cadastrado.
		return postagemRepository.findById(postagem.getId())
		.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem)))//função lambda
            	.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public void deletePostagem(@PathVariable Long id){
		Optional<Postagem> postagem = postagemRepository.findById(id);
				
				if(postagem.isEmpty())
					throw new ResponseStatusException(HttpStatus.NOT_FOUND);
				
		       postagemRepository.deleteById(id);//status 204.checar se existe antes de apagar
		  
		       //outra forma de fazer @DeleteMapping
		       
		       /*@DeleteMapping("/{id}")
		   	public ResponseEntity deletePostagem(@PathVariable Long id){
		           return postagemRepository.findById(id)
		               .map(resposta ->) {
		                   postagemRepository.deleteById(id);
		                   return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		               }
		               .orElse(ResponseEntity.notFound()).build());
		               }*/
		       
			
		
	}
}


