package com.aiinterview.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ResumeParserService {

    public String extractText(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null) return "";

        String text;
        if (fileName.toLowerCase().endsWith(".pdf")) {
            text = extractTextFromPdf(file.getInputStream());
        } else if (fileName.toLowerCase().endsWith(".docx")) {
            text = extractTextFromDocx(file.getInputStream());
        } else {
            throw new IllegalArgumentException("Unsupported file format. Please upload PDF or DOCX.");
        }

        if (!isValidResume(text)) {
            throw new IllegalArgumentException("This file does not appear to be a valid resume. Looking for sections like Experience, Education, or Skills.");
        }

        return text;
    }

    private boolean isValidResume(String text) {
        if (text == null || text.isBlank()) return false;
        String lowercaseText = text.toLowerCase();
        
        // Keywords to identify a resume
        String[] keywords = {"experience", "education", "skills", "projects", "certifications", "work history", "academic"};
        int matchCount = 0;
        
        for (String keyword : keywords) {
            if (lowercaseText.contains(keyword)) {
                matchCount++;
            }
        }
        
        // Require at least 2 key resume sections
        return matchCount >= 2;
    }

    private String extractTextFromPdf(InputStream inputStream) throws IOException {
        try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractTextFromDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
            return extractor.getText();
        }
    }
}
