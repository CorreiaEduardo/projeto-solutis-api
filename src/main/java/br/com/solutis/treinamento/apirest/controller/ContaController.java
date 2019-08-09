package br.com.solutis.treinamento.apirest.controller;

import br.com.solutis.treinamento.apirest.model.Conta;
import br.com.solutis.treinamento.apirest.service.ContaService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/contas")
public class ContaController {

    private ContaService service;
    @Autowired
    public ContaController(ContaService service) {
        this.service = service;
    }

    @GetMapping
    @ApiOperation(value = "Retorna uma lista de todas as contas cadastradas.")
    public ResponseEntity obterContas(Pageable pageable){
        return this.service.buscarTodas(pageable);
    }

    @PostMapping
    @ApiOperation(value = "Salva uma nova conta.")
    public ResponseEntity inserirConta(@RequestBody Conta c){
        return this.service.inserirConta(c);
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Atualiza uma conta fixa existente.")
    public ResponseEntity atualizarConta(@RequestBody Conta c, @PathVariable("id") Long id){
        return this.service.atualizarConta(c,id);
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Deleta uma conta existente.")
    public ResponseEntity pagarConta(@PathVariable("id") Long id){
        return this.service.deletarContaPorId(id);
    }

}
