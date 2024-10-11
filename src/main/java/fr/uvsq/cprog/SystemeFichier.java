package fr.uvsq.cprog;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * Représente le système de fichiers et fournit des fonctionnalités
 * pour interagir avec les fichiers et les répertoires.
 */
public class SystemeFichier {
  public static File repertoireCourant = new File(System.getProperty("user.dir"));
  
  /**
  * Définit le répertoire courant avec le répertoire spécifié.
  *
  * @param nouveauRepertoire Le nouveau répertoire à définir comme répertoire courant.
  * @throws IllegalArgumentException Si le chemin fourni n'est pas un répertoire valide.
  */
  public static void setRepertoireCourant(File nouveauRepertoire) {
    if (nouveauRepertoire != null && nouveauRepertoire.isDirectory()) {
      repertoireCourant = nouveauRepertoire;
    } else {
      throw new IllegalArgumentException("Le chemin fourni n'est pas un répertoire valide.");
    }
  }

  public static String getRepertoireCourantString() {
    return repertoireCourant.toString();
  }

  public static File getRepertoireCourant() {
    return repertoireCourant;
  }

  public static Path getCheminPath() {
    return Paths.get(repertoireCourant.toString());
  }

  /**
  * Affiche le contenu du répertoire courant, ainsi que le chemin absolu
  * du répertoire courant.
  * Affiche également le dernier NER (Numéro d'Élément de Répertoire)
  * s'il est défini.
  * Appelle ensuite la méthode pour obtenir et afficher le contenu du
  * répertoire courant.
  */
  public static void listerContenuRepertoire() {
    //affiche chemin du rep courant 
    System.out.println(ansi().fg(MAGENTA).a("Répertoire courant : "
        + repertoireCourant.getAbsolutePath()).reset());
    if (Command.dernierNER != -1) {
      //affiche le dernier NER
      System.out.println(ansi().fgBright(GREEN).a("NER courant : "
          + Command.dernierNER).reset());
    }
    // appel la méthode pour obtenir et afficher le rep courant
    afficherContenuRepertoire();
  }

  private static void afficherContenuRepertoire() {
    // récupère tous les fichiers et dossiers présents dans le repertoireCourant
    File[] contenu = repertoireCourant.listFiles();
    if (contenu != null) {
      for (int i = 0; i < contenu.length; i++) {
        File file = contenu[i];
        //Chaque fichier ou dossier est extrait du tableau, et son chemin absolu est récupéré.
        String filePath = file.getAbsolutePath();
        //récupère une annotation associée au chemin du fichier/dossier
        Optional<String> annotationOpt = Command.annotations.getAnnotation(filePath);

        // Affiche le numéro d'index de chaque élément
        System.out.print(ansi().fg(MAGENTA).a("[" + (i + 1) + "] ").reset());

        // Affiche le nom du fichier ou du dossier avec la couleur appropriée
        if (file.isDirectory()) {
          // Colorie le nom du dossier en violet (MAGENTA)
          System.out.print(ansi().fg(MAGENTA).a(file.getName()).reset());
        } else {
          // Colorie le nom du fichier en blanc (DEFAULT)
          System.out.print(ansi().fg(DEFAULT).a(file.getName()).reset());
        }

        // Affiche l'annotation s'il y en a une, en cyan
        if (annotationOpt.isPresent()) {
          System.out.print(ansi().fg(CYAN).a(" - Note: ").reset() + annotationOpt.get());
        }
        System.out.println(); // Nouvelle ligne à la fin
      }

      // Vérifie et affiche l'annotation du dernier NER courant, si présente
      if (Command.dernierNER != -1 && Command.dernierNER <= contenu.length) {
        File file = contenu[Command.dernierNER - 1];
        String filePath = file.getAbsolutePath();
        Optional<String> annotationOpt = Command.annotations.getAnnotation(filePath);
        if (annotationOpt.isPresent()) {
          System.out.println(ansi().fg(CYAN).a("Note du NER courant [" + Command.dernierNER + "]: "
              + annotationOpt.get()).reset());
        }
      }
    } else {
      System.out.println("Le répertoire est vide ou ne peut pas être lu.");
    }
  }

    
  /**
  * Change le répertoire courant pour le répertoire correspondant au numéro NER fourni.
  * Si le numéro NER correspond à un répertoire, déplace le répertoire courant vers
  * ce répertoire.
  * Sinon, affiche un message d'erreur.
  * Si le numéro NER est invalide ou hors de la plage, affiche un message d'erreur.
  *
  * @param ner Le numéro NER du répertoire à entrer.
  */
  public static void entrerDansRepertoire(int ner) {
    File[] contenu = repertoireCourant.listFiles();
    if (contenu != null && ner > 0 && ner <= contenu.length) {
      File element = contenu[ner - 1];
      if (element.isDirectory()) {
        repertoireCourant = element;
      } else {
        System.out.println("L'élément sélectionné [" + ner + "] n'est pas un répertoire.");
      }
    } else {
      System.out.println("NER invalide ou hors limites.");
    }
  }

  /**
  * Affiche le contenu d'un fichier ou la taille d'un dossier,
  * en fonction du numéro NER fourni.
  * Si le numéro NER correspond à un fichier texte (.txt, .md, .adoc, .json),
  * affiche son contenu.
  * Sinon, affiche la taille du fichier.
  * Si le numéro NER est invalide ou hors de la plage, affiche un message d'erreur.
  *
  * @param ner Le numéro NER du fichier ou du dossier à visualiser.
  */
  public static void visu(int ner) {
    File[] contenu = repertoireCourant.listFiles();
    if (contenu != null && ner > 0 && ner <= contenu.length) {
      File element = contenu[ner - 1];
      if (element.isFile()) {
        String nomFichier = element.getName();
        if (nomFichier.endsWith(".txt") || nomFichier.endsWith(".md")
            || nomFichier.endsWith(".adoc") || nomFichier.endsWith(".json")) {
          try {
            String contenuFichier = new
                String(Files.readAllBytes(Paths.get(element.getPath())), StandardCharsets.UTF_8);
            System.out.println(contenuFichier);
          } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier: " + e.getMessage());
          }
        } else {
          System.out.println("La taille du fichier '"
              + element.getName() + "' est: " + element.length() + " octets.");
        }
      } else {
        System.out.println("Cet élement n'est pas un fichier");
      } 
    } else {
      System.out.println("Le NER entré est invalide.");
    }
  }
    
  /**
  * Méthode principale du programme.
  * Affiche le contenu du répertoire courant, puis teste la
  * navigation dans un sous-répertoire,
  * la visualisation du contenu d'un fichier ou la taille d'un dossier,
  * et enfin retourne au répertoire parent.
  *
  * @param args Les arguments de la ligne de commande.
  */
  public static void main(String[] args) {
    // Affiche le contenu du répertoire courant
    listerContenuRepertoire();

    // Test de la navigation dans un sous-répertoire
    entrerDansRepertoire(4);

    // Test de la visualisation du contenu d'un fichier ou de la taille d'un dossier
    visu(10);

    // Retour au répertoire parent
    entrerDansRepertoireParent();
  }

  /**
  * Change le répertoire courant pour le répertoire parent, s'il existe.
  * Si le répertoire parent n'existe pas, affiche un message indiquant
  * que vous êtes déjà à la racine.
  */
  public static void entrerDansRepertoireParent() {
    File parent = repertoireCourant.getParentFile();
    if (parent != null) {
      repertoireCourant = parent;
    } else {
      System.out.println("Vous êtes déjà à la racine.");
    }
  }   
}