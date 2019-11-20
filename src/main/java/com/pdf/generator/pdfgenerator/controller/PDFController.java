package com.pdf.generator.pdfgenerator.controller;

import com.pdf.generator.pdfgenerator.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class PDFController {

    @Autowired
    PDFService pdfService;

    @GetMapping("/download")
    public String generatePdf(HttpServletResponse response) throws Exception {
        return pdfService.generatePDF(response);
    }
}
