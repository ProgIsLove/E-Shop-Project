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
    private final CountryRepository countryRepository;

    public StateService(StateRepository stateRepository, StateMapper stateMapper, CountryRepository countryRepository) {
        this.stateRepository = stateRepository;
        this.stateMapper = stateMapper;
        this.countryRepository = countryRepository;
    }

    public List<StateResponse> getStateByCountry(Integer countryId) {
        return stateMapper.convertToDTO(stateRepository.findByIdOrderByNameAsc(countryId));
    }

    public StateResponse addState(StateRequest stateRequest) {
        State state = stateMapper.convertToEntity(stateRequest);
        State stateSaved = stateRepository.save(state);
        return stateMapper.convertEntityToResponse(stateSaved);
    }

    @Transactional
    public void delete(Integer id) throws StateNotFoundException {
        Long countById = countryRepository.countById(id);
        if (countById == 0) {
            throw new StateNotFoundException("State not found");
        }

        countryRepository.deleteById(id);
    }
}
