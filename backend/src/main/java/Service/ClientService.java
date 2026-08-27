package Service;

import java.util.stream.Collectors;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import DTO.ClientDto;
import Entity.Clients;
import Mapper.ClientMapper;
import Repository.ClientsRepository;

@Service
public class ClientService {

    private final ClientsRepository clientRepository;
private final ClientMapper clientMapper;

public ClientService(ClientsRepository clientRepository, ClientMapper clientMapper) {
    this.clientRepository = clientRepository;
    this.clientMapper = clientMapper;
}

//avoir tous les clients
public List<ClientDto> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<ClientDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clientRepository.findAll(pageable).map(clientMapper::toDTO);
    }
  //rechercher par raison sociale
    public Page<ClientDto> searchByRaisonSociale(String raisonsociale, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return clientRepository.findByRaisonsocialeContainingIgnoreCase(raisonsociale, pageable)
            .map(clientMapper::toDTO);
}
// rechercher par identifiant
     public ClientDto findById(Long id) {
        Clients client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'id : " + id));
        return clientMapper.toDTO(client);
    }
 //creer client
    public ClientDto create(ClientDto dto) {
        Clients client = clientMapper.toEntity(dto);
        return clientMapper.toDTO(clientRepository.save(client));
    }
    //modifier client
    public ClientDto update(Long id, ClientDto dto) {
        Clients client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'id : " + id));
        clientMapper.updateEntityFromDTO(dto, client);
        return clientMapper.toDTO(clientRepository.save(client));
    }
    //desactiver client

     public void deactivate(Long id) {
        Clients client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'id : " + id));
        client.setActif(false);
        clientRepository.save(client);
    }





}

