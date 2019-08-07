package br.com.solutis.treinamento.apirest.controller;

import br.com.solutis.treinamento.apirest.model.Conta;
import br.com.solutis.treinamento.apirest.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping(path = "/api/contas")
public class ContaController {

    private ContaService service;
    @Autowired
    public ContaController(ContaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Conta> obterContas(){
        return this.service.buscarTodas();
    }

    @PostMapping
    public ResponseEntity inserirConta(@RequestBody Conta c){
        return this.service.inserirConta(c);
    }

    @PutMapping
    public ResponseEntity atualizarConta(@RequestBody Conta c){
        if (c.isFixo()) return this.service.atualizarConta(c);
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity pagarConta(@PathVariable("id") Long id){
        return this.service.deletarContaPorId(id);
    }

}
