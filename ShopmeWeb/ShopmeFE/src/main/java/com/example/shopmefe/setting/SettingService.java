package com.example.shopmefe.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingsCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {

    private final SettingRepository settingRepository;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public List<Setting> getGeneralSettings() {
        return settingRepository.findByTwoCategories(SettingsCategory.GENERAL, SettingsCategory.CURRENCY);
    }

}
