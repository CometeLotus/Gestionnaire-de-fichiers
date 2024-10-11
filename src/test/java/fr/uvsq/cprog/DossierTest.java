package fr.uvsq.cprog;


import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;


import org.junit.jupiter.api.Test;

public class DossierTest{
    static Path chemin = Paths.get("miniprojet-grp-77_78");
    static Path cheminAbsolu = chemin.toAbsolutePath().getParent();
    static String cheminProjet = cheminAbsolu.toString().replace("\\", "/");

    @Test
    public void testdirExiste() {
        Dossier d = new Dossier("test.txt", cheminProjet);
        assertTrue(d.dirExiste());
        d.rm();
    }

    @Test
    public void testdirExisteDossierNonExistant() {
        Dossier d = new Dossier("test.txt", cheminProjet);
        d.rm();
        assertFalse(d.dirExiste());
    }

    @Test
    public void testdirExisteStatic(){
        String nomDoss = "test";
        File dos = new File(cheminProjet + "/" + nomDoss);
        if (!dos.exists()){
            dos.mkdir();
        }else{
            System.out.println("Erreur lors de la création du dossier.");
        }
        assertTrue(Dossier.dirExiste(nomDoss, cheminProjet));
        dos.delete();
    }

    @Test
    public void testdirExisteStaticDossierNonExistant(){
        String nomDoss = "test";
        new File(cheminProjet + "/" + nomDoss);
        assertFalse(Dossier.dirExiste(nomDoss, cheminProjet));
    }

    @Test
    public void testEstVraimentVide(){
        Dossier d = new Dossier("test", cheminProjet);
        assertTrue(Dossier.estVide(d.getCheminComplet()));
        d.rm();
    }

    @Test
    public void testNEstPasVide(){
        Dossier d = new Dossier("test", cheminProjet);
        Dossier e = new Dossier("testPasVide", cheminProjet + "/test");
        assertFalse(Dossier.estVide(d.getCheminComplet()));
        d.rm();
        e.rm();
    }
}