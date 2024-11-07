package com.example.shopmebe.setting.state;

import com.shopme.common.entity.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StateMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    List<StateResponse> convertToDTO(List<State> states);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "country.id", source = "countryDTO.id")
    @Mapping(target = "country.name", source = "countryDTO.countryName")
    State convertToEntity(StateRequest stateRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "countryDTO.id", source = "country.id")
    @Mapping(target = "countryDTO.countryName", source = "country.name")
    StateResponse convertEntityToResponse(State state);
}
