package fr.uvsq.cprog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;


/**
 * Représente un fichier dans le système de fichiers.
 * Étend la classe Entite pour hériter des fonctionnalité
 * communes à toutes les entités du système.
 */
public class Fichier extends Entite {
  long taille;
  String extension;
  
  /**
  * Constructeur de la classe Fichier.
  *
  * @param nomfich Le nom du fichier.
  * @param chemin Le chemin du fichier.
  */
  public Fichier(String nomfich, String chemin) {
    super(chemin);
    File fic = new File(chemin, nomfich);
    //Si le Fichier n'existe pas, on doit le créer
    if (!fic.exists()) {
      //On crée physiquement le fichier sur le disque
      try {
        fic.createNewFile();
      } catch (IOException e) {
        System.out.println("Erreur lors de la création du fichier : " + e.getMessage());
      }
    }

    this.nom = nomfich;
    this.chemin = chemin;
    this.datecreation = LocalDateTime.now();
    this.dernieremodif = LocalDateTime.now();
    this.extension = FilenameUtils.getExtension(chemin + "/" + nomfich);
    this.taille = fic.length();
  }

  public Fichier(String chemin) {
    super(chemin);
  }

  public boolean fileExiste() {
    File fichier = new File(getCheminComplet());
    return fichier.exists();
  }

  public static boolean fileExiste(String nom, String cheminPere) {
    File fichier = new File(cheminPere + "/" + nom);
    return fichier.exists();
  }

  @Override
  public void copy() {
    // On vide le Presse-Papier pour ne pas corrompre les données copiées
    Utils.viderPressePapier();

    // Obtient le chemin du "Presse-Papier"
    String destination = cheminProjet + "/Presse-Papier" + "/" + this.nom;

    // On obtient le chemin du répertoire source
    Path source = Paths.get(this.chemin + "/" + this.nom);

    Path destinationPath = Paths.get(destination);
    try {
      Files.copy(source, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean rm() {
    super.rm();
    File aaSupprimer = new File(getCheminComplet());
    if (aaSupprimer.delete()) {
      return true;
    }
    return false;
  }

  @Override
  public void paste(String destination) throws IOException {
    Path pressePapier = Paths.get("Presse-Papier");
    Path destinationPath = Paths.get(destination);

    try (Stream<Path> pressePapierContent = Files.list(pressePapier)) {
      Optional<Path> firstElement = pressePapierContent.findFirst();

      // Vérifie si le Presse-Papier est vide
      if (firstElement.isPresent()) {
        Path firstItem = firstElement.get();

        // Obtient le nom du fichier du Presse-Papier
        String fileName = firstItem.getFileName().toString();
        String extension = "";

        // Si le fichier a une extension, on la récupère
        if (fileName.contains(".")) {
          extension = fileName.substring(fileName.lastIndexOf("."));
        }

        // Construction du chemin de destination pour le fichier
        Path targetPath = destinationPath.resolve(fileName);

        // Si le fichier existe déjà à la destination, on le renomme
        int index = 1;
        while (Files.exists(targetPath)) {
          String nomDeBase = fileName.substring(0, fileName.lastIndexOf('.'));
          String nouveauNom = nomDeBase + "-copy-" + index + extension;
          targetPath = destinationPath.resolve(nouveauNom);
          index++;
        }

        // Copie du fichier du Presse-Papier vers la destination
        Files.copy(firstItem, targetPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Élément collé avec succès !");
      } else {
        System.out.println("Le Presse-Papier est vide. Aucun élément à coller.");
      }
    }
  }
}