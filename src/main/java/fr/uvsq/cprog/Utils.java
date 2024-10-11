package fr.uvsq.cprog;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
* Classe utilitaire contenant des méthodes pour effectuer diverses opérations
* sur les fichiers et les répertoires.
*/
public class Utils {

  static final Path cheminp = Paths.get("miniprojet-grp-77_78");
  static final Path cheminAbsolu = cheminp.toAbsolutePath().getParent();
  static final String cheminProjet = cheminAbsolu.toString().replace("\\", "/");

  /**
  * Retourne un chemin qui n'existe pas déjà dans le système de fichiers.
  * Si le chemin initial existe déjà, il ajoute "-copie-" suivi d'un indice
  * au nom du dossier jusqu'à ce
  * qu'un chemin inexistant soit trouvé.
  *
  * @param initialPath Le chemin initial à partir duquel commencer.
  * @return Un chemin qui n'existe pas dans le système de fichiers.
  */
  public static Path getNonexistentPath(Path initialPath) {
    Path newPath = initialPath;
    int index = 1;

    // Tant que le chemin existe déjà, ajoute un indice au nom du dossier
    while (Files.exists(newPath)) {
      String newName = initialPath.getFileName() + "-copie-" + index;
      newPath = initialPath.resolveSibling(newName);
      index++;
    }

    return newPath;
  }

  /**
  * Vide le presse-papiers en supprimant tous les fichiers et dossiers qu'il contient.
  */
  public static void viderPressePapier() {
    File pressePapier = new File(cheminProjet + "/Presse-Papier");
    
    File[] contenu = pressePapier.listFiles();
    if (contenu != null) {
      for (File fichier : contenu) {
        if (fichier.isDirectory()) {
          Dossier.supprimerContenuDossier(fichier);
        }
        // Supprimer le fichier ou le dossier
        fichier.delete();
      }
    }
  }
}