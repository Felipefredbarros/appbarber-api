package com.appbarber.api.service;

import com.appbarber.api.dto.BarbeariaRequest;
import com.appbarber.api.model.Barbearia;
import com.appbarber.api.model.Contato;
import com.appbarber.api.model.Endereco;
import com.appbarber.api.model.Usuario;
import com.appbarber.api.repository.BarbeariaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BarbeariaService {

    private final BarbeariaRepository repository;

    public BarbeariaService(BarbeariaRepository repository) {
        this.repository = repository;
    }

    @Transactional // se um falhar, nada é salvo no banco
    public Barbearia register(BarbeariaRequest dados, Usuario donoLogado) {
        Barbearia barbearia = new Barbearia();
        barbearia.setNome(dados.nome());
        barbearia.setDescricao(dados.descricao());
        barbearia.setImg(dados.img());
        barbearia.setDono(donoLogado);
        barbearia.setFormasPagamento(dados.formasPagamento());

        Endereco end = new Endereco();
        end.setRua(dados.endereco().rua());
        end.setNumero(dados.endereco().numero());
        end.setComplemento(dados.endereco().complemento());
        end.setBairro(dados.endereco().bairro());
        end.setCep(dados.endereco().cep());

        //fazer depois
        //end.setCidade(cidadeRepository.findById(dados.endereco().cidadeId()).get());

        barbearia.setEndereco(end);

        dados.contatos().forEach(dto -> {
            Contato c = new Contato();
            c.setNumero(dto.numero());
            c.setTipoTelefone(dto.tipo());
            c.setPrincipal(dto.principal());
            barbearia.addContato(c);
        });

        return repository.save(barbearia);
    }
}
