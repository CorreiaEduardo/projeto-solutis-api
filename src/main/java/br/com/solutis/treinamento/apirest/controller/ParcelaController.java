package br.com.solutis.treinamento.apirest.controller;

import br.com.solutis.treinamento.apirest.service.ParcelaService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController()
@RequestMapping(path = "/api/parcelas")
public class ParcelaController {

    private ParcelaService service;
    @Autowired
    public ParcelaController(ParcelaService service) {
        this.service = service;
    }

    @GetMapping
    @ApiOperation(value = "Retorna uma lista das parcelas de um dado mes para uma conta cadastrada.")
    public ResponseEntity buscarParcelasPorMes(@RequestParam String vencimento){
        return this.service.gerarResumoParaVencimento(vencimento);
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Deleta uma parcela cadastrada.")
    public ResponseEntity pagarParcela(@PathVariable("id") Long id){
        return this.service.deletarPorId(id);
    }
}
