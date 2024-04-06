package com.learning.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.learning.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.learning.rest.webservices.restfulwebservices.jpa.UserRepository;

//import jakarta.servlet.Servlet;
import jakarta.validation.Valid;

@RestController
public class UserJpaResource 
{
	
	private final UserRepository repository;
	private final PostRepository postRepository;
	
	public UserJpaResource(UserRepository repository,PostRepository postRepository)
	{
		
		this.repository=repository;
		this.postRepository=postRepository;
	}
	
   //GET /users
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers()
	{
		return repository.findAll();
	}
	
	//EntityModel
	//WebMvcLinkBuilder      
	//doing these things for hateoas 
	// wrapped User class and made it an entity model
	
	//GET /users/1  (getting specific users)
		@GetMapping("/jpa/users/{id}")
		public EntityModel<User> retrieveUser(@PathVariable int id)
		{
			Optional<User> user = repository.findById(id);
			
			if(user.isEmpty())
				throw new UserNotFoundException("id:"+id);
			
			EntityModel<User> entityModel = EntityModel.of(user.get());
			
			WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
			entityModel.add(link.withRel("all-users"));
			
			return entityModel;
		}
		
	//POST /users
		@PostMapping("/jpa/users") //checking for validation too over here
		public ResponseEntity<User> createUser(@Valid @RequestBody User user) //body of req contains the user data
		{
			User savedUser = repository.save(user);
//			// /users/4 => /users/{id},  user.getID
			URI location =ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(savedUser.getId())
					.toUri(); 
			// location - /users/4̥
			return ResponseEntity.created(location).build();
			//return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
			
		}
		
		@DeleteMapping("/jpa/users/{id}")
		public void deleteUser(@PathVariable int id)
		{
			repository.deleteById(id);
			System.out.println("Deleted");
		}
		
		@GetMapping("/jpa/users/{id}/posts")
		public List<Post> retrievePostsForUser(@PathVariable int id)
		{
Optional<User> user = repository.findById(id);
			
			if(user.isEmpty())
				throw new UserNotFoundException("id:"+id);
			return user.get().getPosts();
			
		}
		
		@PostMapping("/jpa/users/{id}/posts")
		public ResponseEntity<Object> createPostForUser(@PathVariable int id,@Valid @RequestBody Post post)
		{
Optional<User> user = repository.findById(id);
			
			if(user.isEmpty())
				throw new UserNotFoundException("id:"+id);
			
			post.setUser(user.get());
		 Post savedPost = postRepository.save(post);
			
			URI location =ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(savedPost.getId())
					.toUri(); 
			// location - /users/4̥
			return ResponseEntity.created(location).build();
						
		}
		
}
