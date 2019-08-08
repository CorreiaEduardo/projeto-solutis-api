package br.com.solutis.treinamento.apirest.util;

import br.com.solutis.treinamento.apirest.model.Parcela;
import br.com.solutis.treinamento.apirest.service.ParcelaService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class TimeThread extends Thread{

    private ParcelaService service;
    @Autowired
    public TimeThread(ParcelaService service) {
        this.service = service;
    }

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();
        int diasParaFimMes = now.getMonth().length(now.toLocalDate().isLeapYear()) - now.getDayOfMonth() + 1;
        long milisegundosParaFimMes = (diasParaFimMes * 1440) * 60000L;
//        long miliToNextMinute = (60 - now.getSecond())*1000L;
        try {
            sleep(milisegundosParaFimMes);
//            sleep(miliToNextMinute);
        } catch (Exception ignored) {

        }
        this.service.inserirParcela( new Parcela() );
        //lógica para inserir novo registro no parcela
    }
}
