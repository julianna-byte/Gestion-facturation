package Mapper;

import org.springframework.stereotype.Component;
import DTO.ClientDto;
import Entity.Clients;


@Component

public class ClientMapper {

 //Dto(Data Transfert Object) en entités
    public ClientDto toDTO(Clients client) {
        ClientDto dto = new ClientDto();
        dto.setIdClient(client.getIdClient());
        dto.setRccm(client.getRCCM());
        dto.setRaisonsociale(client.getRaisonsocial());
        dto.setAdresse(client.getAdresse());
        dto.setVille(client.getVille());
        dto.setPays(client.getPays());
        dto.setTelephone(client.getTelephone());
        dto.setEmail(client.getEmail());
        dto.setNomcontact(client.getNomcontact());
        dto.setNif(client.getNIF());
        return dto;
    }

    // Entités en  Dto(Data Transfert Object) 
     public Clients toEntity(ClientDto dto) {
        Clients client = new Clients();
        client.setRCCM(dto.getRccm());
        client.setRaisonsocial(dto.getRaisonsociale());
        client.setAdresse(dto.getAdresse());
        client.setVille(dto.getVille());
        client.setPays(dto.getPays());
        client.setTelephone(dto.getTelephone());
        client.setEmail(dto.getEmail());
        client.setNomcontact(dto.getNomcontact());
        client.setNIF(dto.getNif());
        return client;

    }
    // Mise a jour d'entité 

    public void updateEntityFromDTO(ClientDto dto, Clients client) {
        client.setRCCM(dto.getRccm());
        client.setRaisonsocial(dto.getRaisonsociale());
        client.setAdresse(dto.getAdresse());
        client.setVille(dto.getVille());
        client.setPays(dto.getPays());
        client.setTelephone(dto.getTelephone());
        client.setEmail(dto.getEmail());
        client.setNomcontact(dto.getNomcontact());
        client.setNIF(dto.getNif());
    }    


}
