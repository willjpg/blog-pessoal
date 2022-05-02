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

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

@RestController
	@RequestMapping("/temas")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	public class TemaController {

		@Autowired
		private TemaRepository temaRepository;
		
		@GetMapping
		public ResponseEntity <List<Tema>> getAll(){
			return ResponseEntity.ok(temaRepository.findAll());
			
			// select * from tb_temas;
		}
		
		//buscar uma id
		@GetMapping("/{id}")
		public ResponseEntity <Tema> getById(@PathVariable Long id){
			return temaRepository.findById(id)
					.map(resposta -> ResponseEntity.ok(resposta))//função lambda
					.orElse(ResponseEntity.notFound().build());
			
			// select * from tb_temas where id = id
		}
		@GetMapping("/descricao/{descricao}")
		public ResponseEntity <List<Tema>> getByDescricao(@PathVariable String descricao){
			return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao));
			
			// select * from tb_temas where descricao like "%descricao%";
			
		}
		
		@PostMapping			//Created 201.status	//save. salva ou atualiza
		public ResponseEntity <Tema> postTema(@Valid @RequestBody Tema tema){
			return ResponseEntity.status(HttpStatus.CREATED).body(temaRepository.save(tema));
			
		}
		
		@PutMapping		//Put para atualizar		//CREATED vira OK				//save. salva ou atualiza
		public ResponseEntity <Tema> putTema(@Valid @RequestBody Tema tema){//confirmar que nâo há id cadastrado.
			return temaRepository.findById(tema.getId())
			.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(temaRepository.save(tema)))//função lambda
	            	.orElse(ResponseEntity.notFound().build());
		}
		
		@DeleteMapping("/{id}")
		@ResponseStatus (HttpStatus.NO_CONTENT)
		public void deleteTema(@PathVariable Long id){
			Optional<Tema> tema = temaRepository.findById(id);
					
					if(tema.isEmpty())
						throw new ResponseStatusException(HttpStatus.NOT_FOUND);
					
			       temaRepository.deleteById(id);//status 204.checar se existe antes de apagar
			  
			       //outra forma de fazer @DeleteMapping
			       
			       /*@DeleteMapping("/{id}")
			   	public ResponseEntity deleteTema(@PathVariable Long id){
			           return TemaRepository.findById(id)
			               .map(resposta ->) {
			                   TemaRepository.deleteById(id);
			                   return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			               }
			               .orElse(ResponseEntity.notFound()).build());
			               }*/
			       
				
			
		}
	}


