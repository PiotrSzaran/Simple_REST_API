package simple_rest_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import simple_rest_api.model.Category;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisementDto {
    private Long id;
    private Category category;
    private String title;
    private String message;
    private LocalDateTime publicationDate;
    private Boolean isPromoted;
    private LocalDateTime promotionDate;
    private Boolean isActivated;
    private Long userId;
}