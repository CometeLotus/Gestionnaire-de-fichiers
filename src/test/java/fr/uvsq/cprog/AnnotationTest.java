package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

class AnnotationTest {

    private Annotation annotation;
    static final Path cheminp = Paths.get("miniprojet-grp-77_78");
    static final Path cheminAbsolu = cheminp.toAbsolutePath().getParent();
    static final String cheminProjet = cheminAbsolu.toString().replace("\\", "/");

    @BeforeEach
    void setUp() {
        // Initialiser la classe Annotation avant chaque test
        annotation = new Annotation();
    }

    /*@Test
    void testAjoutAnnotation() {
        // Ajouter une annotation et vérifier qu'elle est présente
        annotation.ajoutAnnotation(1, "Ceci est une annotation");
        Optional<String> retrievedAnnotation = annotation.getAnnotation(cheminProjet + "/.git");
        assertTrue(retrievedAnnotation.isPresent(), retrievedAnnotation.toString());
        assertEquals("Ceci est une annotation", retrievedAnnotation.get());
    }*/

    @Test
    void testSuppressionAnnotation() {
        // Ajouter une annotation, la supprimer, puis vérifier qu'elle n'est plus présente
        annotation.ajoutAnnotation(1, "Annotation à supprimer");
        annotation.suppAnnotation(1);
        Optional<String> retrievedAnnotation = annotation.getAnnotation(cheminProjet + "/.git");
        assertFalse(retrievedAnnotation.isPresent());
    }

    /*@Test
    void testChargementAnnotations() {
        // Ajouter une annotation, sauvegarder, recharger, puis vérifier qu'elle est toujours présente
        annotation.ajoutAnnotation(1, "Annotation à charger");
        annotation.saveAnnotations();
        annotation = new Annotation(); // recharger
        Optional<String> retrievedAnnotation = annotation.getAnnotation(cheminProjet + "/.git");
        assertTrue(retrievedAnnotation.isPresent());
        assertEquals("Annotation à charger", retrievedAnnotation.get());
    }*/
}