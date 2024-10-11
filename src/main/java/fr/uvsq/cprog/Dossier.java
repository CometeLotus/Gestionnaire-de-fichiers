package fr.uvsq.cprog;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* Représente un dossier dans le système de fichiers.
* Cette classe étend la classe abstraite Entite et fournit des fonctionnalités spécifiques
* pour la manipulation des dossiers.
*/
public class Dossier extends Entite {

  public boolean dirExiste() {
    File dossier = new File(this.chemin, this.nom);
    return dossier.exists() && dossier.isDirectory();
  }

  public static boolean dirExiste(String nom, String cheminPere) {
    File dossier = new File(cheminPere + "/" + nom);
    return dossier.exists() && dossier.isDirectory();
  }

  /**
  * Construit un objet de type Dossier avec le nom et le chemin spécifiés.
  * Si le dossier physique correspondant n'existe pas, il est créé.
  *
  * @param nom Le nom du dossier.
  * @param chemin Le chemin du dossier.
  */
  public Dossier(String nom, String chemin) {
    super(chemin);
    File dossierPhysique = new File(chemin, nom);
    if (!dossierPhysique.exists()) {
      if (dossierPhysique.mkdir()) {
        System.out.println("Le dossier a été créé avec succès.");
      } else {
        System.out.println("Le dossier existe déjà ou la création a échoué.");
      }
    }
    this.nom = nom;
    this.chemin = chemin;
    this.datecreation = LocalDateTime.now();
    this.dernieremodif = LocalDateTime.now();
    new Fichier("notes.json", chemin + "/" + nom);
  }

  public Dossier(String chemin) {
    super(chemin);
  }

  /**
  * Vérifie si le répertoire spécifié est vide.
  *
  * @param cheminComplet Le chemin complet du répertoire à vérifier.
  * @return true si le répertoire est vide, sinon false.
  */
  public static boolean estVide(String cheminComplet) {
    File dossier = new File(cheminComplet);

    // On vérifie si le chemin correspond à un dossier
    if (dossier.isDirectory()) {
      // On récupére la liste des fichiers et dossiers dans le dossier
      String[] contenu = dossier.list();

      // Si la liste est vide ou que la taile du dossier est 0 alors cela renvoie true 
      return contenu == null || contenu.length == 0;
    }

    // Si le chemin ne correspond pas à un dossier, renvoyer false
    return false;
  }

  @Override
  public boolean rm() {
  
    // Obtient le chemin du répertoire à supprimer
    Path source = Paths.get(this.chemin + "/" + this.nom);

    try {
      // Parcours récursif du dossier et suppression de chaque élément
      Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          Files.delete(file); // Supprimer le fichier
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
          Files.delete(dir); // Supprimer le répertoire
          return FileVisitResult.CONTINUE;
        }
      });
        
      return true; // La suppression s'est déroulée avec succès
    } catch (IOException e) {
      e.printStackTrace();
      return false; // La suppression a échoué
    }
  }
  
  /**
  * Supprime récursivement le contenu d'un dossier.
  *
  * @param dossier Le dossier dont le contenu doit être supprimé.
  */
  public static void supprimerContenuDossier(File dossier) {
    File[] contenuDossier = dossier.listFiles();
    if (contenuDossier != null) {
      // Supprimer le contenu du dossier récursivement
      for (File f : contenuDossier) {
        if (f.isDirectory()) {
          supprimerContenuDossier(f);
        }
        // Supprimer le fichier
        f.delete();
      }
    }
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
      // Cas où l'entité à copier est un dossier.
      Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
        public FileVisitResult preVisitDirectory(Path doss, BasicFileAttributes attrs)
            throws IOException {
          Path relativePath = source.relativize(doss);
          Path targetPath = destinationPath.resolve(relativePath);
          Files.createDirectories(targetPath);
          return FileVisitResult.CONTINUE;
        }

        public FileVisitResult visitFile(Path fic, BasicFileAttributes attrs) throws IOException {
          Path relativePath = source.relativize(fic);
          Path targetPath = destinationPath.resolve(relativePath);
          Files.copy(fic, targetPath, StandardCopyOption.REPLACE_EXISTING);
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void paste(String destination) throws IOException {
    Path pressePapierDir = Paths.get("Presse-Papier");
    File[] liste = pressePapierDir.toFile().listFiles();

    if (liste != null && liste.length > 0) {
      Path pressePapier = liste[0].toPath();
      Path destinationPath = Paths.get(destination);

      // Obtient le nom du dossier du Presse-Papier
      String dossierName = pressePapier.getFileName().toString();

      // Construction du chemin de destination pour le dossier
      Path initialTargetPath = destinationPath.resolve(dossierName);

      // Si le dossier existe déjà à la destination, on le renomme
      Path targetPath = Utils.getNonexistentPath(initialTargetPath);

      // Création du dossier de destination
      Files.createDirectories(targetPath);

      // Utilisation de Files.walkFileTree pour copier récursivement l'arborescence
      Files.walkFileTree(pressePapier, new SimpleFileVisitor<Path>() {
        public FileVisitResult preVisitDirectory(Path doss, BasicFileAttributes attrs)
            throws IOException {
          Path relativePath = pressePapier.relativize(doss);
          Path cible = targetPath.resolve(relativePath);
          Files.createDirectories(cible);
          return FileVisitResult.CONTINUE;
          }

        public FileVisitResult visitFile(Path fic, BasicFileAttributes attrs) throws IOException {
          Path relativePath = pressePapier.relativize(fic);
          Path fichierCible = targetPath.resolve(relativePath);

          // Renomme le fichier si déjà existant à la destination
          fichierCible = Utils.getNonexistentPath(fichierCible);

          Files.copy(fic, fichierCible, StandardCopyOption.REPLACE_EXISTING);
          return FileVisitResult.CONTINUE;
          }
        });
    } else {
      System.out.println("Le Presse-Papier est vide. Aucun élément à coller.");
    }
  }

  /**
  * Recherche les fichiers dans un répertoire contenant le nom de fichier spécifié.
  *
  * @param repertoireCourant Le répertoire dans lequel effectuer la recherche.
  * @param nomFichier Le nom du fichier à rechercher.
  * @return indique si fichier trouvé ou un message si aucun fichier n'est trouvé.
  * @throws IOException Si une erreur d'entrée/sortie se produit lors de la recherche.
  */
  public static String find(final Path repertoireCourant,
      final String nomFichier) throws IOException {
    List<Path> foundFiles = new ArrayList<>();
    Files.walkFileTree(repertoireCourant, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(final Path file,
          final BasicFileAttributes attrs) throws IOException {
        if (file.getFileName().toString().contains(nomFichier)) {
          foundFiles.add(file);
        }
        return FileVisitResult.CONTINUE;
      }
    });

    if (foundFiles.isEmpty()) {
      return "Aucun fichier trouvé contenant : " + nomFichier;
    } else {
      StringBuilder result = new StringBuilder("Fichiers "
          + "trouvés contenant '" + nomFichier + "' : ");
      for (Path path : foundFiles) {
        result.append(path.toAbsolutePath()).append(", ");
      }
      result.delete(result.length() - 2, result.length());
      return result.toString();
    }
  }
}