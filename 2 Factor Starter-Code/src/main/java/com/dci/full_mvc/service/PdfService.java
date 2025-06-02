package com.dci.full_mvc.service;

import com.dci.full_mvc.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final SpringTemplateEngine springTemplateEngine;

    public byte[] generatePdf(List<Movie> movies){
        Context context = new Context();

        context.setVariable("movies",movies);

        String renderedHtml = springTemplateEngine.process("pdf/movies",context);

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(renderedHtml);
            renderer.layout();
            renderer.createPDF(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
