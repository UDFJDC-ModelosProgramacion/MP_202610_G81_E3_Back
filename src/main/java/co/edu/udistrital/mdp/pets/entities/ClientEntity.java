package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.OneToMany;
//import jakarta.persistence.OneToOne;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class ClientEntity extends BaseEntity {
    
    private String clientName ;
    private String clientPhone;
    private String clientEmail;

    @PodamExclude
    @OneToMany(mappedBy = "client")
    private List<NotificationEntity> notifications = new ArrayList<>();

  //  @OneToOne(mappedBy = "client")
    //private AdopterEntity adopter;

  //Se añade relacion entre mensaje, cliente y shelter.
  @PodamExclude
  @OneToMany(mappedBy = "client")
  private List<MessageEntity> messages = new ArrayList<>();

}
