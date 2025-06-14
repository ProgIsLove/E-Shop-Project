package com.example.shopmebe.setting;

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

    public List<Setting> allSettings() {
        return (List<Setting>) settingRepository.findAll();
    }

    public GeneralSettingBag generalSettingBag() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = settingRepository.findByCategory(SettingsCategory.GENERAL);
        List<Setting> currencySettings = settingRepository.findByCategory(SettingsCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new GeneralSettingBag(settings);
    }

    public void saveAll(Iterable<Setting> settings) {
        settingRepository.saveAll(settings);
    }

    public List<Setting> getMailServerSettings() {
        return settingRepository.findByCategory(SettingsCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplateSettings() {
        return settingRepository.findByCategory(SettingsCategory.MAIL_TEMPLATES);
    }
}
