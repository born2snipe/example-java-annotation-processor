package com.github.born2snipe.spring.web;

import org.springframework.stereotype.Controller;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SpringAnnotatedControllerIndexer extends AbstractProcessor {
    public static final String INDEX_FILENAME = "META-INF/controller.idx";
    private ArrayList<TypeElement> controller = new ArrayList<TypeElement>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Controller.class)) {
            if (!(element instanceof TypeElement)) {
                continue;
            }

            TypeElement typeElement = (TypeElement) element;
            String message = "Controller found in " + processingEnv.getElementUtils().getBinaryName(typeElement).toString();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
            controller.add(typeElement);
        }

        write();

        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new HashSet<String>();
        annotationTypes.add(Controller.class.getName());
        return annotationTypes;
    }

    private void write() {
        Set<String> entries = new HashSet<String>();
        for (TypeElement typeElement : controller) {
            entries.add(processingEnv.getElementUtils().getBinaryName(typeElement).toString());
        }

        write(entries);
    }

    private void write(Set<String> entries) {
        try {
            FileObject file = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", INDEX_FILENAME);
            Writer writer = file.openWriter();
            for (String entry : entries) {
                writer.write(entry);
                writer.write("\n");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            // it's the first time, create the file
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }
}
