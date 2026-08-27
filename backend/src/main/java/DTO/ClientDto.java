package DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;


@Data


public class ClientDto {

    private Long idClient;

   @NotBlank(message = "Le RCCM est obligatoire")
    private String rccm;

    @NotBlank(message = "La raison sociale est obligatoire")
    private String raisonsociale;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @NotBlank(message = "Le pays est obligatoire")
    private String pays;

     @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le nom du contact est obligatoire")
    private String nomcontact;

    @NotBlank(message = "Le NIF est obligatoire")
    private String nif;

    private Boolean actif;

}
