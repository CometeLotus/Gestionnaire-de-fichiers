package fr.uvsq.cprog;


import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;


import org.junit.jupiter.api.Test;

public class FichierTest{
    static Path chemin = Paths.get("miniprojet-grp-77_78");
    static Path cheminAbsolu = chemin.toAbsolutePath().getParent();
    static String cheminProjet = cheminAbsolu.toString().replace("\\", "/");

    @Test
    public void testfileExiste() {
        Fichier f = new Fichier("test.txt", cheminProjet);
        assertTrue(f.fileExiste());
        f.rm();
    }

    @Test
    public void testfileExisteFichierNonExistant() {
        Fichier f = new Fichier("test.txt", cheminProjet);
        f.rm();
        assertFalse(f.fileExiste());
    }

    @Test
    public void testfileExisteStatic(){
        String nomFic = "test.txt";
        File fic = new File(cheminProjet + "/" + nomFic);
        try{
            fic.createNewFile();
        }catch (IOException e){
            System.out.println("Erreur lors de la création du fichier.");
        }
        assertTrue(Fichier.fileExiste(nomFic, cheminProjet));
        fic.delete();
    }

    @Test
    public void testfileExisteStaticFichierNonExistant(){
        String nomFic = "test.txt";
        new File(cheminProjet + "/" + nomFic);
        assertFalse(Fichier.fileExiste(nomFic, cheminProjet));
    }
}