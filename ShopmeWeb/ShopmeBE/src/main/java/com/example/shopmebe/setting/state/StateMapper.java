package com.example.shopmebe.setting.state;

import com.shopme.common.entity.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "stateMapper")
public interface StateMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    List<StateResponse> convertToDTO(List<State> states);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    State convertToEntity(StateRequest stateRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StateResponse convertEntityToResponse(State state);
}
