package br.com.cotiinformatica.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.cotiinformatica.dtos.PostLoginDTO;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.helpers.MD5Helper;
import br.com.cotiinformatica.repositories.UsuarioRepository;
import br.com.cotiinformatica.security.TokenSecurity;

@Controller
public class LoginController {

	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody PostLoginDTO dto) {

		try {
			
			//pesquisar o usuário no banco de dados através do email e da senha
			UsuarioRepository usuarioRepository = new UsuarioRepository();
			Usuario usuario = usuarioRepository.findByEmailAndSenha(dto.getEmail(), MD5Helper.encrypt(dto.getSenha()));
			
			//verificar se o usuário foi encontrado..
			if(usuario != null) {
				return ResponseEntity
						.status(HttpStatus.OK)
						.body(TokenSecurity.generateToken(usuario.getEmail()));
			}
			else {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Acesso negado. Usuário inválido.");
			}
		}
		catch(Exception e) {
			
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
		
	}

}
