package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Classe de base pour la traçabilité (RG cahier des charges 2.5) :
 * date de création, date de modification et auteur sur chaque enregistrement.
 * Les entités qui ont besoin de traçabilité étendent cette classe.
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)

public abstract class Auditable {

     @CreatedDate
    @Column(name = "datecreation", updatable = false)
    private LocalDateTime dateCreation;

    @LastModifiedDate
    @Column(name = "datemodification")
    private LocalDateTime dateModification;

    @CreatedBy
    @Column(name = "auteur", updatable = false)
    private String auteur;

}
