package shopme.setting;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.setting.SettingRepository;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingsCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import shopme.testcontainers.AbstractIntegrationTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeBeApplication.class)
public class SettingRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private SettingRepository settingRepository;


    @Test
    public void testCreateGeneralSettings() {
        Setting siteName = new Setting("SITE_NAME", "Shopme", SettingsCategory.GENERAL);
        Setting siteLogo = new Setting("SITE_LOGO", "Shopme.png", SettingsCategory.GENERAL);
        Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2021 Shopme Ltd.", SettingsCategory.GENERAL);
        settingRepository.saveAll(List.of(siteName, siteLogo, copyright));

        Iterable<Setting> iterable = settingRepository.findAll();

        assertThat(iterable).size().isPositive();
        assertThat(iterable).size().isGreaterThan(0);
    }

    @Test
    public void testCreateCurrencySettings() {
        Setting currencyId = new Setting("CURRENCY_ID", "1", SettingsCategory.CURRENCY);
        Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingsCategory.CURRENCY);
        Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingsCategory.CURRENCY);
        Setting decimalPointType = new Setting("CURRENCY_POINT_TYPE", "POINT", SettingsCategory.CURRENCY);
        Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingsCategory.CURRENCY);
        Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingsCategory.CURRENCY);

        settingRepository.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType,
                decimalDigits, thousandsPointType));

        Iterable<Setting> iterable = settingRepository.findAll();
    }
}
