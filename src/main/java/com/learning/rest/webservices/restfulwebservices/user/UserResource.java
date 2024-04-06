package com.learning.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.Servlet;
import jakarta.validation.Valid;

@RestController
public class UserResource 
{
	private UserDaoService service;
	
	public UserResource(UserDaoService service)
	{
		this.service=service;
	}
	
   //GET /users
	@GetMapping("/users")
	public List<User> retrieveAllUsers()
	{
		return service.findAll();
	}
	
	//EntityModel
	//WebMvcLinkBuilder      
	//doing these things for hateoas 
	// wrapped User class and made it an entity model
	
	//GET /users/1  (getting specific users)
		@GetMapping("/users/{id}")
		public EntityModel<User> retrieveUser(@PathVariable int id)
		{
			User user = service.findOne(id);
			
			if(user==null)
				throw new UserNotFoundException("id:"+id);
			
			EntityModel<User> entityModel = EntityModel.of(user);
			
			WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
			entityModel.add(link.withRel("all-users"));
			
			return entityModel;
		}
		
	//POST /users
		@PostMapping("/users") //checking for validation too over here
		public ResponseEntity<User> createUser(@Valid @RequestBody User user) //body of req contains the user data
		{
			User savedUser = service.save(user);
			// /users/4 => /users/{id},  user.getID
			URI location = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(savedUser.getId())
					.toUri(); 
			// location - /users/4
			return ResponseEntity.created(location).build();
			
		}
		
		@DeleteMapping("/users/{id}")
		public void deleteUser(@PathVariable int id)
		{
			service.deleteById(id);
		}
		
}
