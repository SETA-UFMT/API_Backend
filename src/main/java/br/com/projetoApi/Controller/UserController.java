package br.com.projetoApi.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import br.com.projetoApi.Model.User;
import br.com.projetoApi.Repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuário", description = "Endpoints relacionados ao usuário")
public class UserController {

     @GetMapping("/teste")
     @Operation(summary = "Teste da API", description = "Verifica se a API está funcionando")
    public String testar() {
        return "Funcionando!";
    }

    // @Autowired
    // private UserRepository userRepository;

    // @PostMapping
    // public User createUser(@RequestBody User user) {
    //     return userRepository.save(user);
    // }

    // @GetMapping
    // public List<User> listUsers() {
    //     return userRepository.findAll();
    // }

    // @PostMapping("/login")
    // public String login(@RequestBody User user) {
    //     return userRepository.findById(user.getCpf())
    //             .filter(u -> u.getSenha().equals(user.getSenha()))
    //             .map(u -> "Login bem-sucedido")
    //             .orElse("CPF ou senha inválidos");
    // }
}
