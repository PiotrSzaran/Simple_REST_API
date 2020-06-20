package simple_rest_api.validators;

import org.springframework.stereotype.Component;
import simple_rest_api.exceptions.AppException;
import simple_rest_api.model.dto.CreateAdvertisementDto;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Component
public class AdvertisementValidator extends AbstractValidator<CreateAdvertisementDto> {

    @Override
    public Map<String, String> validate(CreateAdvertisementDto createAdvertisementDto) {
        errors.clear();

        if (Objects.isNull(createAdvertisementDto)) {
            errors.put("advertisement", "object is null");
        }

        if (!isTitleValid(createAdvertisementDto.getTitle())) {
            errors.put("title", "title is incorrect");
        }

        if (!isMessageValid(createAdvertisementDto.getMessage())) {
            errors.put("message", "message is incorrect");
        }

        if (Objects.isNull(createAdvertisementDto.getCategory())) {
            errors.put("category", "category is null");
        }

        if ((containsBadContent(createAdvertisementDto.getTitle()))) {
            errors.put("title", " title contains banned content");
        }

        if ((containsBadContent(createAdvertisementDto.getMessage()))) {
            errors.put("message", " message contains banned content");
        }

        return errors;
    }

    private boolean isTitleValid(String title) {
        return Objects.nonNull(title) && title.matches("[A-Za-z\\d\\s]+");
    }

    private boolean isMessageValid(String message) {
        return Objects.nonNull(message) && message.matches("[A-Za-z\\d\\s,;.]+");
    }

    private boolean containsBadContent(String text) {

        if (Objects.isNull(text)) {
            throw new AppException("text is null");
        }

        var message = text.toLowerCase();
        var badContent = Arrays.asList("http", "www", "pomidor");

        int counter = 0;
        for (String badWord: badContent
        ) {
            if (message.contains(badWord)) {
                counter++;

            }
        }

        return counter > 0;
    }
}
