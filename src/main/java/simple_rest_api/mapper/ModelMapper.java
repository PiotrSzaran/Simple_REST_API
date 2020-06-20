package simple_rest_api.mapper;

import simple_rest_api.model.Advertisement;
import simple_rest_api.model.User;
import simple_rest_api.model.dto.AdvertisementDto;
import simple_rest_api.model.dto.CreateAdvertisementDto;
import simple_rest_api.model.dto.CreateUserDto;
import simple_rest_api.model.dto.UserDto;

public interface ModelMapper {

    static User fromCreateUserDtoToUser(CreateUserDto createUserDto) {
        return User.builder()
                .name(createUserDto.getName())
                .surname(createUserDto.getSurname())
                .username(createUserDto.getUsername())
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .role(createUserDto.getRole())
                .enabled(createUserDto.getEnabled())
                .build();
    }

    static UserDto fromUserToUserDto(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }

    static Advertisement fromCreateAdvertisementDtoToAdvertisement(CreateAdvertisementDto createAdvertisementDto) {
        return Advertisement
                .builder()
                .category(createAdvertisementDto.getCategory())
                .title(createAdvertisementDto.getTitle())
                .message(createAdvertisementDto.getMessage())
                .isPromoted(createAdvertisementDto.getIsPromoted())
                .build();
    }

    static AdvertisementDto fromAdvertisementToAdvertisementDto(Advertisement advertisement) {
        return AdvertisementDto
                .builder()
                .id(advertisement.getId())
                .category(advertisement.getCategory())
                .title(advertisement.getTitle())
                .message(advertisement.getMessage())
                .isActivated(advertisement.getIsActivated())
                .publicationDate(advertisement.getPublicationDate())
                .isPromoted(advertisement.getIsPromoted())
                .promotionDate(advertisement.getPromotionDate())
                .userId(advertisement.getUser().getId())
                .build();
    }
}
