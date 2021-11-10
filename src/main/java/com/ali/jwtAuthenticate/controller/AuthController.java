package com.ali.jwtAuthenticate.controller;

import com.ali.jwtAuthenticate.message.request.LoginFrom;
import com.ali.jwtAuthenticate.message.request.SignUpForm;
import com.ali.jwtAuthenticate.message.response.JwtResponse;
import com.ali.jwtAuthenticate.model.Role;
import com.ali.jwtAuthenticate.model.RoleName;
import com.ali.jwtAuthenticate.model.Users;
import com.ali.jwtAuthenticate.repository.RoleRepository;
import com.ali.jwtAuthenticate.repository.UsersRepository;
import com.ali.jwtAuthenticate.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public Object signIn(@Valid @RequestBody LoginFrom form) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getEmail(),
                        form.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    public Object signUp(@Valid @RequestBody SignUpForm form) {
        if(userRepository.existsByEmail(form.getEmail())) {
            return new ResponseEntity<String>("Fail,Email is already in use!",
                    HttpStatus.BAD_REQUEST);
        }
        if(!form.getPassword().equals(form.getConfirm_password())){
            return new ResponseEntity<String>("Fail,Passwords are not same",HttpStatus.BAD_REQUEST);
        }


        // Creating user's account
        Users user = new Users(form.getName(),
                form.getEmail(), encoder.encode(form.getPassword()));

        Set<String> strRoles = form.getRole();
        Set<Role> roles = new HashSet<>();

        //if set a role, user role should be user.
        if(strRoles == null){
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            roles.add(userRole);
            user.setRoles(roles);
            userRepository.save(user);
            return ResponseEntity.ok().body("User registered successfully!");
        }

        strRoles.forEach(role -> {
        	switch(role) {
	    		case "admin":
	    			Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
	                .orElseThrow(() -> new RuntimeException("Fail, User Role not find."));
	    			roles.add(adminRole);
	    			
	    			break;
	    		case "moderator":
	            	Role pmRole = roleRepository.findByName(RoleName.ROLE_MODERATOR)
	                .orElseThrow(() -> new RuntimeException("Fail, User Role not find."));
	            	roles.add(pmRole);
	            	
	    			break;
	    		default:
	        		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
	                .orElseThrow(() -> new RuntimeException("Fail, User Role not find."));
	        		roles.add(userRole);        			
        	}
        });
        
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<String>("User registered successfully",HttpStatus.OK);
    }
}