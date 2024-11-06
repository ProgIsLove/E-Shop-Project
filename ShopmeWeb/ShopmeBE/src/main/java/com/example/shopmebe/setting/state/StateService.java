package com.example.shopmebe.setting.state;

import com.example.shopmebe.exception.StateNotFoundException;
import com.example.shopmebe.setting.country.CountryRepository;
import com.shopme.common.entity.State;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StateService {

    private final StateRepository stateRepository;
    private final StateMapper stateMapper;

    public StateService(StateRepository stateRepository, StateMapper stateMapper) {
        this.stateRepository = stateRepository;
        this.stateMapper = stateMapper;
    }

    private State getStateById(Integer stateId) throws StateNotFoundException {
        return stateRepository.findById(stateId)
                .orElseThrow(() -> new StateNotFoundException("State not found"));
    }

    public List<StateResponse> getStateByCountry(Integer countryId) {
        return stateMapper.convertToDTO(stateRepository.findByCountryIdOrderByNameAsc(countryId));
    }

    public StateResponse addState(StateRequest stateRequest) {
        State state = stateMapper.convertToEntity(stateRequest);
        State stateSaved = stateRepository.save(state);
        return stateMapper.convertEntityToResponse(stateSaved);
    }

    @Transactional
    public StateResponse updateState(StateRequest stateRequest, Integer stateId) throws StateNotFoundException {
        return Optional.ofNullable(this.getStateById(stateId)).map(oldState -> {
            oldState.setName(stateRequest.name().trim());
            State updateState = stateRepository.save(oldState);
            return stateMapper.convertEntityToResponse(updateState);
        }).orElseThrow(() -> new StateNotFoundException("State not found"));
    }

    @Transactional
    public void delete(Integer id) throws StateNotFoundException {
        Long countById = stateRepository.countById(id);
        if (countById == 0) {
            throw new StateNotFoundException("State not found");
        }

        stateRepository.deleteById(id);
    }
}
