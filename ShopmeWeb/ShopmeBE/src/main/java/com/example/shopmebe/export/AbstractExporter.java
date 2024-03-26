package com.example.shopmebe.export;

import jakarta.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

abstract class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response, String extension, String contentType,
                                  String prefix) {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        String fileName = prefix + timestamp + extension;

        response.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }
}
