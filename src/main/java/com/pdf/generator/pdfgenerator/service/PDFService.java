package com.pdf.generator.pdfgenerator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.pdf.generator.pdfgenerator.bean.PDFBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PDFService {

    public static final String EXPIRES = "Expires";
    public static final String COLON = ":";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

    @Autowired
    SpringTemplateEngine templateEngine;

    @Autowired
    ObjectMapper objectMapper;

    public String generatePDF(HttpServletResponse response) throws Exception {
        String html = getQuoteHtml();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.createPdf(html, bos);
        byte[] outArray = bos.toByteArray();
        String fileName = "Sample.pdf";
        response.reset();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(outArray.length);
        response.setHeader(EXPIRES + COLON, "0");
        response.setHeader(CONTENT_DISPOSITION, ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
        FileCopyUtils.copy(outArray, response.getOutputStream());

        bos.flush();
        bos.close();

        return "";
    }

    private String getQuoteHtml() {
        PDFBean pdfBean = new PDFBean();
        pdfBean.setAge("1");
        pdfBean.setEmail("test@test.com");
        pdfBean.setName("Test");

        Map<String, Object> variable = objectMapper.convertValue(pdfBean, Map.class);
        Context context = new Context();
        context.setVariables(variable);
        return templateEngine.process("pdf_template", context);
    }

    public static void createPdf(String str, ByteArrayOutputStream outByteStream) throws DocumentException {
        final ITextRenderer iTextRenderer = new ITextRenderer();
        iTextRenderer.setDocumentFromString(str);
        iTextRenderer.layout();
        iTextRenderer.createPDF(outByteStream);
    }
}
