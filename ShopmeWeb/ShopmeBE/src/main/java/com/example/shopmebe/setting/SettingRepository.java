package com.example.shopmebe.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingsCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {

    List<Setting> findByCategory(SettingsCategory category);
}
