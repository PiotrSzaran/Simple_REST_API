package simple_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simple_rest_api.model.dto.CreateAdvertisementDto;
import simple_rest_api.model.dto.response.ResponseData;
import simple_rest_api.service.CreateAdvertisementService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/create")
public class CreateAdvertisementController {

    private final CreateAdvertisementService createAdvertisementService;

    @PostMapping
    public ResponseData<Long> create(@RequestBody CreateAdvertisementDto createAdvertisementDto) {

        return ResponseData.<Long>builder().data(createAdvertisementService.create(createAdvertisementDto)).build();
    }
}
