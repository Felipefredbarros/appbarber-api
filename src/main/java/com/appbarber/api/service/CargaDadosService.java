package com.appbarber.api.service;

import com.appbarber.api.model.Cidade;
import com.appbarber.api.model.Estado;
import com.appbarber.api.repository.CidadeRepository;
import com.appbarber.api.repository.EstadoRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CargaDadosService {

    private final EstadoRepository estadoRepository;
    private final CidadeRepository cidadeRepository;

    public CargaDadosService(EstadoRepository estadoRepository, CidadeRepository cidadeRepository) {
        this.estadoRepository = estadoRepository;
        this.cidadeRepository = cidadeRepository;
    }
    //com esse comando o metodo so roda se o projeto estiver 100% startado
    @EventListener(ApplicationReadyEvent.class)
    public void carregarDadosIniciais() {
        if (estadoRepository.count() > 0) {
            System.out.println("Banco já populado.");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        String urlEstados = "https://servicodados.ibge.gov.br/api/v1/localidades/estados";

        // busca Estados na api, ele vai ate o link do IBGE, le o json e transforma em uma lista
        List<Map<String, Object>> estadosIbge = restTemplate.getForObject(urlEstados, List.class);

        for (Map<String, Object> dadosEstado : estadosIbge) {
            Estado estado = new Estado();
            estado.setNome((String) dadosEstado.get("nome"));
            estado.setSigla((String) dadosEstado.get("sigla"));
            estadoRepository.save(estado);

            // busca Cidades deste Estado
            String urlCidades = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/" + estado.getSigla() + "/municipios";
            List<Map<String, Object>> cidadesIbge = restTemplate.getForObject(urlCidades, List.class);

            for (Map<String, Object> dadosCidade : cidadesIbge) {
                Cidade cidade = new Cidade();
                cidade.setNome((String) dadosCidade.get("nome"));
                cidade.setEstado(estado);
                cidadeRepository.save(cidade);
            }
            System.out.println("Finalizado: " + estado.getNome());
        }
    }

}
