package com.learning.rest.webservices.restfulwebservices.helloworld;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//REST API
@RestController
public class HelloWorldController 
{
  // /hello-world
   @GetMapping(path="/hello-world")	
   public String helloWorld()
   {
	   return "Hello World!";
   }
   
   @GetMapping(path="/hello-world-bean")	
   public HelloWorldBean helloWorldBean()
   {
	   return new HelloWorldBean("Hello World!");
   }
   
    // Path Parameters
    // /users/{id}/todos/{id} => /users/2/todos/200
    // /hello-world/path-variable/{name}
   
   @GetMapping(path="/hello-world/path-variable/{name}")	
   public HelloWorldBean helloWorldPathVariable(@PathVariable String name)
   {
	   return new HelloWorldBean(String.format("Hello World, %s",name));
   }
   
   private MessageSource messageSource;
   
   //constructor injection
   public HelloWorldController(MessageSource messageSource)
   {
	   this.messageSource=messageSource;
   }
   
   // Internationalization
   @GetMapping(path="/hello-world-internationalized")	
   public String helloWorldInternationalized()
   {
	   Locale locale = LocaleContextHolder.getLocale();
	   return messageSource.getMessage("good.morning.message", null,"Default Message",locale);
	   
   }
   
}
