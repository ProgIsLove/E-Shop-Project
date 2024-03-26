package com.example.shopmebe.export;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExport extends AbstractExporter {

    public void export(List<User> users, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, ".csv", "text/csv", "users_");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "E-mail", "First Name", "Last name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
        csvWriter.writeHeader(csvHeader);

        for (User user : users) {
            csvWriter.write(user, fieldMapping);
        }

        csvWriter.close();
    }
}
