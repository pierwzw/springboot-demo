package com.pier.service;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author zhongweiwu
 * @date 2019/9/20 14:36
 */
public interface PdfExportService {

    void make(Map<String, Object> model, Document document,
              PdfWriter writer, HttpServletRequest request,
              HttpServletResponse response);
}
