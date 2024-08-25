package com.example.shopmefe.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingsCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SettingRepository extends CrudRepository<Setting, String> {

    List<Setting> findByCategory(SettingsCategory category);

    @Query("SELECT s FROM Setting s WHERE s.category = :categoryOne OR s.category = :categoryTwo")
    List<Setting> findByTwoCategories(@Param("categoryOne") SettingsCategory categoryOne,
                                      @Param("categoryTwo") SettingsCategory categoryTwo);

}
