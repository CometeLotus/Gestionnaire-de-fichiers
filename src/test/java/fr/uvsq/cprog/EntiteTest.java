package fr.uvsq.cprog;


import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class EntiteTest{
    static Path chemin = Paths.get("miniprojet-grp-77_78");
    static Path cheminAbsolu = chemin.toAbsolutePath().getParent();
    static String cheminProjet = cheminAbsolu.toString().replace("\\", "/");

    @Test
    public void rmFichierTest() {
        // On spécifie le nom du fichier et le chemin du dossier
        String nomFichier = "test.txt";
        String cheminDossier = cheminProjet;

        // On crée une instance de la classe Fichier
        Fichier fichier = new Fichier(nomFichier,cheminDossier);

        // On supprime le fichier et vérifie si la suppression réussit
        assertTrue(fichier.rm());
    }

    @Test 
    public void rmDossierTest(){
        Dossier d = new Dossier("test", cheminProjet);
        assertTrue(d.rm());
    }

    @Test
    public void rmArborscence(){ //On supprime un ensemble de fichiers/dossiers à partir du répertoire racine
        Dossier d = new Dossier("test", cheminProjet);
        new Dossier("test1", cheminProjet + "/test");
        new Fichier("test.md", cheminProjet + "/test");
        assertTrue(d.rm());
    }

    @Test
    public void viderPressePapierTest(){
        new Dossier("test", cheminProjet + "/Presse-Papier");
        Utils.viderPressePapier();
        assertTrue(Dossier.estVide(cheminProjet + "/Presse-Papier"));
    }

    @Test 
    public void copyFichier(){
        Fichier f = new Fichier("test.txt", cheminProjet);
        f.copy();
        assertTrue(Fichier.fileExiste("test.txt",cheminProjet + "/Presse-Papier")); 
        f.rm();
        Utils.viderPressePapier();
    }


    @Test
    public void copyDossierTest(){
        Dossier d = new Dossier("test", cheminProjet);
        d.copy();
        assertTrue(Dossier.dirExiste("test",cheminProjet + "/Presse-Papier"));
        d.rm();
        Utils.viderPressePapier();
    }

    @Test
    public void copyArborescenceTest(){ 
        Dossier d = new Dossier("test", cheminProjet);
        new Dossier("test1", cheminProjet + "/test");
        new Fichier("test.md", cheminProjet + "/test");
        d.copy();
        Boolean condition = Dossier.dirExiste("test", cheminProjet + "/Presse-Papier")
        && Dossier.dirExiste("test1", cheminProjet + "/Presse-Papier/test") 
        && Fichier.fileExiste("test.md", cheminProjet + "/Presse-Papier/test");
        assertTrue(condition);
        d.rm();
        Utils.viderPressePapier();
    }

    @Test 
    public void pasteFichierTest(){
        Fichier f = new Fichier("toto.txt", cheminProjet);
        f.copy();
        Entite.paste(cheminProjet + "/home");
        assertTrue(Fichier.fileExiste("toto.txt",cheminProjet + "/home"));
        f.rm();
        Utils.viderPressePapier();
        new Fichier("toto.txt", cheminProjet + "/home").rm();
    }

    @Test 
    public void pasteFichierExistantTest(){
        Fichier f = new Fichier("toto.txt", cheminProjet);
        f.copy();
        Entite.paste(cheminProjet + "/home");
        Entite.paste(cheminProjet + "/home");
        boolean condition = Fichier.fileExiste("toto.txt",cheminProjet + "/home")
        && Fichier.fileExiste("toto-copy-1.txt",cheminProjet + "/home");
        assertTrue(condition);
        f.rm();
        Utils.viderPressePapier();
        new Fichier("toto.txt", cheminProjet + "/home").rm();
        new Fichier("toto-copy-1.txt", cheminProjet + "/home").rm();
    }

    @Test 
    public void pasteDossierTest(){
        Dossier d = new Dossier("test", cheminProjet);
        d.copy();
        Entite.paste(cheminProjet + "/home");
        assertTrue(Dossier.dirExiste("test",cheminProjet + "/home"));
        d.rm();
        Utils.viderPressePapier();
        new Dossier("test", cheminProjet + "/home").rm();
    }

    @Test 
    public void pasteDossierExistantTest(){
        Dossier d = new Dossier("test", cheminProjet);
        d.copy();
        //Création du dossier dans home
        Entite.paste(cheminProjet + "/home");
        //Création de la copie du dossier dans home
        Entite.paste(cheminProjet + "/home");
        assertTrue(Dossier.dirExiste("test-copy-1",cheminProjet + "/home"));
        d.rm();
        Utils.viderPressePapier();
        //Nettoyage de home
        File f = new File(cheminProjet + "/home/test");
        f.delete();
        File g = new File(cheminProjet + "/home/test-copy-1");
        g.delete();
    }

    @Test
    public void pasteArborescenceTest(){
        // On crée l'arborescence
        Dossier d1 = new Dossier("racineArbre", cheminProjet);
        Dossier d2 = new Dossier("test", cheminProjet + "/racineArbre");
        Dossier d3 = new Dossier("wow", cheminProjet + "/racineArbre/test");
        Fichier f = new Fichier("toto.txt", cheminProjet + "/racineArbre/test/wow");
        Fichier g = new Fichier("tata.txt", cheminProjet + "/racineArbre/test/wow");

        // On copie
        d1.copy();
        
        // On colle
        Entite.paste(cheminProjet + "/home");

        // On vérifie que l'arborescence a bien été collée en entiereté dans le répertoire destination
        boolean condition = Dossier.dirExiste("racineArbre", cheminProjet + "/home")
        && Dossier.dirExiste("test", cheminProjet + "/home/racineArbre")
        && Dossier.dirExiste("wow", cheminProjet + "/home/racineArbre/test")
        && Fichier.fileExiste("toto.txt", cheminProjet + "/home/racineArbre/test/wow")
        && Fichier.fileExiste("tata.txt", cheminProjet + "/home/racineArbre/test/wow");
        System.out.println(cheminProjet);
        //On teste la condition
        assertTrue(condition, cheminProjet + "/home");

        //On nettoie
        d1.rm();
        d2.rm();
        d3.rm();
        f.rm();
        g.rm();
        Utils.viderPressePapier();
        new Dossier("racineArbre", cheminProjet + "/home").rm();
    }

    @Test
    public void pasteArborescenceExistanteTest(){
        // On crée l'arborescence
        Dossier d1 = new Dossier("racineArbre", cheminProjet);
        Dossier d2 = new Dossier("test", cheminProjet + "/racineArbre");
        Dossier d3 = new Dossier("wow", cheminProjet + "/racineArbre/test");
        Fichier f = new Fichier("toto.txt", cheminProjet + "/racineArbre/test/wow");
        Fichier g = new Fichier("tata.txt", cheminProjet + "/racineArbre/test/wow");

        // On copie
        d1.copy();

        // On colle
        Entite.paste(cheminProjet + "/home");
        Entite.paste(cheminProjet + "/home");
        // On vérifie que l'arborescence a bien été collée en entiereté dans le répertoire destination
        boolean condition = Dossier.dirExiste("racineArbre-copy-1", cheminProjet + "/home")
        && Dossier.dirExiste("test", cheminProjet + "/home/racineArbre")
        && Dossier.dirExiste("wow", cheminProjet + "/home/racineArbre/test")
        && Fichier.fileExiste("toto.txt", cheminProjet + "/home/racineArbre/test/wow")
        && Fichier.fileExiste("tata.txt", cheminProjet + "/home/racineArbre/test/wow");

        //On teste la condition
        assertTrue(condition);

        //On nettoie
        d1.rm();
        d2.rm();
        d3.rm();
        f.rm();
        g.rm();
        Utils.viderPressePapier();
        new Dossier("racineArbre", cheminProjet + "/home").rm();
        new Dossier("racineArbre-copy-1",cheminProjet + "/home").rm();
    }

    @Test 
    public void cutFichierExistant(){
        Fichier f = new Fichier("test.txt", cheminProjet);
        f.cut();
        boolean condition = !f.fileExiste() && Fichier.fileExiste("test.txt",cheminProjet + "/Presse-Papier");
        assertTrue(condition);
        Utils.viderPressePapier();
    }

    @Test 
    public void cutFichierNonExistant(){
        Fichier f = new Fichier("test.txt", cheminProjet);
        f.rm();
        f.cut();
        boolean condition = !f.fileExiste() && !Fichier.fileExiste("test.txt",cheminProjet + "/Presse-Papier");
        assertTrue(condition);
    }

    @Test
    public void cutDossierExistant(){
        Dossier d = new Dossier("test", cheminProjet);
        d.cut();
        boolean condition = !d.dirExiste() && Dossier.dirExiste("test",cheminProjet + "/Presse-Papier");
        assertTrue(condition);
        Utils.viderPressePapier();
    }

    @Test
    public void cutDossierNonExistant(){
        Dossier d = new Dossier("test", cheminProjet);
        d.rm();
        d.cut();
        boolean condition = !d.dirExiste() && !Dossier.dirExiste("test",cheminProjet + "/Presse-Papier");
        assertTrue(condition);
    }

    @Test
    public void cutArborescence(){
        Dossier d = new Dossier("test", cheminProjet);
        new Fichier("nul.txt", cheminProjet + "/test");
        new Dossier("wow", cheminProjet + "/test");
        new Fichier("Bien.txt", cheminProjet + "/test/wow");
        d.cut();
        boolean condition = !d.dirExiste() && Dossier.dirExiste("test",cheminProjet + "/Presse-Papier");
        assertTrue(condition);
        Utils.viderPressePapier();
    }

    //Faire un test sur une arborescence inéxistante est inutile car cela équivaut à un dossier inéxistant.

    @Test 
    public void findUnFichierExistant(){
        Fichier f = new Fichier("test.txt", cheminProjet + "/home");
        List <String> expected = new ArrayList<>();
        expected.add(cheminProjet + "/home/test.txt");
        assertEquals(expected, Entite.find(cheminProjet, "test.txt"));
        f.rm();
    }

    @Test void findPlusieursFichiersExistants(){
        Fichier f = new Fichier("test.txt", cheminProjet + "/home");
        Fichier g = new Fichier("test.txt", cheminProjet + "/Presse-Papier");
        Fichier h = new Fichier("test.txt", cheminProjet);
        List <String> expected = new ArrayList<>();
        expected.add(cheminProjet + "/home/test.txt");
        expected.add(g.getCheminComplet());
        expected.add(h.getCheminComplet());
        assertEquals(expected, Entite.find(cheminProjet, "test.txt"));
        f.rm();
        g.rm(); 
        h.rm();
    }

    @Test 
    public void findFichierNonExistant(){
        List <String> expected = new ArrayList<>();
        assertEquals(expected, Entite.find(cheminProjet, "test.txt"));
    }

}