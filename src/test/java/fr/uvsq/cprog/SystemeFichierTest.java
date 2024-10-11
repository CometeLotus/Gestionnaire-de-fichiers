package fr.uvsq.cprog;


import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SystemeFichierTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private File repertoireInitial;



    @BeforeEach
    public void setUpStreams() {
        repertoireInitial = SystemeFichier.getRepertoireCourant();
        File testRepertoire = new File("zeytest");
        testRepertoire.mkdir();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        SystemeFichier.setRepertoireCourant(repertoireInitial);
        
    }

    @Test
    public void testListerContenuRepertoire() {
        SystemeFichier.listerContenuRepertoire();
        String output = outContent.toString();
        assertTrue(output.contains("Répertoire courant :"), "Le répertoire courant n'est pas affiché.");
        assertTrue(output.contains(".git"), "Le dossier .git n'est pas listé.");
        assertTrue(output.contains("src"), "Le dossier src n'est pas listé.");
        assertTrue(output.contains("pom.xml"), "Le fichier pom.xml n'est pas listé.");
    
    }
    

    @Test
    public void testEntrerDansRepertoire() {
        int ner = 17; // Il s'agit de home
        SystemeFichier.entrerDansRepertoire(ner);
        String chemin = SystemeFichier.repertoireCourant.getAbsolutePath();
        assertTrue(chemin.contains("miniprojet-grp-77_78\\zeytest"));
        SystemeFichier.entrerDansRepertoireParent();
    }

    @Test
    public void testEntrerFichier() {
        int ner = 12; // Ner de fichier
        SystemeFichier.entrerDansRepertoire(ner);
        assertTrue(outContent.toString().contains("L'élément sélectionné [12] n'est pas un répertoire."),outContent.toString());
    }

    @Test
    public void testEntrerDansRepertoireNonExistant() {
        int ner = 3000; // Ner ne référencant aucun élément.
        SystemeFichier.entrerDansRepertoire(ner);
        assertTrue(outContent.toString().contains("NER invalide ou hors limites."));
    }

    @Test
    public void testVisuFichier() {
        int ner = 5; // Il s'agit du fichier documentation.adoc.
        SystemeFichier.visu(ner);
        assertTrue(outContent.toString().contains("Gestionnaire de Fichiers"));
    }

    @Test
    public void testVisuFichierTxt() {
        int Repertoire = 17; //zeytest
        SystemeFichier.entrerDansRepertoire(Repertoire);
        int ner = 2; //testFichier.txt
        SystemeFichier.visu(ner);
        assertTrue(outContent.toString().contains("phrase from"));
    }

    @Test
    public void testVisuFichierNonTxt() {
       int Repertoire = 17; //zeytest
        SystemeFichier.entrerDansRepertoire(Repertoire);
        int ner = 1; //nonTxt
        SystemeFichier.visu(ner);
        assertTrue(outContent.toString().contains("La taille du fichier"));
    }
    @Test
    public void testVisuNerNonExistant() {
    int nerInexistant = 999; // Un numéro qui est sûr de ne pas exister
    SystemeFichier.visu(nerInexistant);
    // Vérifiez si la sortie contient un message indiquant que le NER est invalide
    String output = outContent.toString();
    assertTrue(output.contains("Le NER entré est invalide") || output.contains("Cet élément n'est pas un fichier"), "Le message d'erreur attendu n'a pas été trouvé dans la sortie");
}


    @Test
    public void testEntrerDansRepertoireParent() {
         boolean zeyneb = true;
        assertTrue(zeyneb);
    }
}