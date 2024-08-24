package com.example.shopmebe.setting;

import com.example.shopmebe.utils.FileUploadUtil;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class SettingController {

    private final SettingService settingService;
    private final CurrencyRepository currencyRepository;


    public SettingController(SettingService settingService, CurrencyRepository currencyRepository) {
        this.settingService = settingService;
        this.currencyRepository = currencyRepository;
    }

    @GetMapping("/settings")
    public String listAll(Model model) {
        List<Setting> settings = settingService.allSettings();
        List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();


        model.addAttribute("currencies", currencies);

        for (Setting setting : settings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) throws IOException {

        GeneralSettingBag generalSettingBag = settingService.generalSettingBag();

        this.saveSiteLogo(multipartFile, generalSettingBag);
        this.saveCurrencySymbol(request, generalSettingBag);
        this.updateSettingValuesForm(request, generalSettingBag.list());

        redirectAttributes.addFlashAttribute("message", "General settings have been saved.");

        return "redirect:/settings";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag generalSettingBag) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            String value = "/site-logo/" + fileName;

            generalSettingBag.updateSiteLogo(value);

            String uploadDir = "../site-logo";
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag generalSettingBag) {
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> currencyByIdResult = currencyRepository.findById(currencyId);

        if (currencyByIdResult.isPresent()) {
            Currency currency = currencyByIdResult.get();
            String symbol = currency.getSymbol();
            generalSettingBag.updateCurrencySymbol(symbol);
        }
    }

    private void updateSettingValuesForm(HttpServletRequest request, List<Setting> settings) {
        for (Setting setting : settings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }

        settingService.saveAll(settings);
    }
}
