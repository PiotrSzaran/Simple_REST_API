package simple_rest_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "advertisements")
public class Advertisement {

    @Id
    @GeneratedValue
    private Long id;

    private Category category;
    private String title;
    private String message;
    private LocalDateTime publicationDate;
    private Boolean isPromoted;
    private LocalDateTime promotionDate; //zoneDate
    private Boolean isActivated;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

}
