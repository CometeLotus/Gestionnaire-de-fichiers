package fr.uvsq.cprog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


//import org.apache.commons.io.FilenameUtils;

/**
* Constructeur de la classe Annotation.
*/
public abstract class Entite {
  String nom;
  String chemin;
  LocalDateTime datecreation;
  LocalDateTime dernieremodif;
  static final Map<String, Integer> dernierNerParDossier = new HashMap<>();
  int ner;

  static final Path cheminp = Paths.get("miniprojet-grp-77_78");
  static final Path cheminAbsolu = cheminp.toAbsolutePath().getParent();
  static final String cheminProjet = cheminAbsolu.toString().replace("\\", "/");
  
  /**
  * Initialise une nouvelle instance de la classe Entite avec le chemin du dossier spécifié.
  *
  * @param cheminDossier Le chemin du dossier associé à cette entité.
  */
  public Entite(String cheminDossier) {
    this.chemin = cheminDossier;
    this.ner = dernierNerParDossier.getOrDefault(cheminDossier, 1);
    dernierNerParDossier.put(cheminDossier, this.ner + 1);
  }

  public String getCheminComplet() {
    return this.chemin + "/" + this.nom;
  }
  
  /**
  * Supprime le fichier ou le dossier représenté par cette entité.
  *
  * @return true si la suppression a réussi, sinon false.
  */
  public boolean rm() {
    File aaSupprimer = new File(getCheminComplet());

    // On vérifie l'existence de ce qu'il y a à supprimer.
    if (!aaSupprimer.exists()) {
      System.out.println("Le fichier ou le dossier n'existe pas : "
          + aaSupprimer.getAbsolutePath());
      return false;
    }

    // Supprime l'annotation pour le dossier/fichier lui-même.
    Command.annotations.supprimerAnnotationParChemin(aaSupprimer.getAbsolutePath());

    if (aaSupprimer.delete()) {
      System.out.println("Suppression réussie : " + aaSupprimer.getAbsolutePath());
      return true;
    } else {
      return false;
    }
  }


  /*
  * Copie l'entité actuelle (fichier ou dossier) vers le presse-papiers.
  */
  public abstract void copy();

  /**
  * Colle l'entité actuelle (fichier ou dossier) dans le répertoire de destination.
  *
  * @param destination Le chemin du répertoire de destination.
  * @throws IOException Si une erreur d'entrée/sortie se produit lors du collage.
  */
  public abstract void paste(String destination) throws IOException;

  /**
  * Effectue une opération de "couper" sur le fichier ou le dossier actuel.
  */
  public void cut() {
    //Un cut est une copie suivie d'une suppression (c'est aussi une des grandes
    //raisons pour laquelle on a implémenté la méthode rm())
    copy();
    rm();
  }
}