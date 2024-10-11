package fr.uvsq.cprog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.*;

/**
* Classe principale pour exécuter les commandes du programme.
*/
public class Command {
  // Variable pour stocker le dernier NER utilisé
  public static int dernierNER = -1;
  // Gestion des annotations
  public static Annotation annotations = new Annotation();
  public static String annotation;
  
  /**
  * Méthode principale du programme.
  *
  * @param args Les arguments de la ligne de commande.
  * @throws IOException En cas d'erreur d'entrée/sortie.
  */
  public static void main(String[] args) throws IOException {
    AnsiConsole.systemInstall();
    Terminal terminal = TerminalBuilder.builder().system(true).build();
    LineReader lineReader = LineReaderBuilder.builder().terminal(terminal)
        .completer(new NullCompleter()).build();
    Annotation notes = new Annotation();
    // Charge les annotations existantes
    notes.chargementAnnotations();

    System.out.println(ansi().fg(BLUE).a("Bienvenue dans l'interface utilisateur !").reset());
    // Affiche le contenu du répertoire courant
    SystemeFichier.listerContenuRepertoire();

    while (true) {
        
      String line = lineReader.readLine(ansi().fg(BLUE)
          .a(" Veuillez entrer une commande > ").reset().toString());
      if ("exit".equalsIgnoreCase(line)) {
        // Ajout du message d'adieu en rouge
        System.out.println(ansi().fg(RED).a("Bye bye !").reset());
        break;
      }
      // Sépare la ligne de commande en "commande" et "arguments"
      String[] sortie = line.split(" ", 3);
      String commande; 
      String argument = "";
    
      // Vérifier si le permier élément  est un nombre (NER)
      if (sortie[0].matches("\\d+")) {
        // Met à jour le dernier NER
        dernierNER = Integer.parseInt(sortie[0]);
        // Si la commande contient plus d'un élément, le deuxième élément est la commande à exécuter
        commande = sortie.length > 1 ? sortie[1] : "";
        // Si la commande contient trois éléments, le troisième est l'argument de la commande
        argument = sortie.length > 2 ? sortie[2] : ""; 
      } else {
        // Si le premier élément n'est pas un nombre, il est considéré comme une commande
        commande = sortie[0];
        // Si la commande contient un deuxième élément, il est considéré comme un argument
        argument = sortie.length > 1 ? sortie[1] : "";
      }
    

      switch (commande.toLowerCase()) {
        case "..":
          SystemeFichier.entrerDansRepertoireParent();
          break;

        case ".":
          SystemeFichier.entrerDansRepertoire(dernierNER);
          break;
        
        case "visu":
          SystemeFichier.visu(dernierNER);
          break;
      
        // Crée un dossier si le nom est spécifié
        case "mkdir":
          if (argument.isEmpty()) {
            System.out.println("Vous n'avez pas renseigner de nom pour la création de dossier.");
          } else if (!argument.isEmpty()) {
            new Dossier(argument, SystemeFichier.getRepertoireCourantString());
          } else {
            System.out.println("Erreur lors de la création du dossier.");
          }
          break;

        //Améliorations
        case "touch":
          if (argument.isEmpty()) {
            System.out.println("Vous n'avez pas renseigner de nom pour la création de fichier.");
          } else if (!argument.isEmpty()) {
            new Fichier(argument, SystemeFichier.getRepertoireCourantString());
          }
          break;

        case "rm":
          if (argument.isEmpty() && dernierNER != -1) {
            System.out.println("Veuillez renseigner le numéro de l'élément à supprimer.");
          }
          if (!argument.isEmpty()) {
            File[] contenu = SystemeFichier.getRepertoireCourant().listFiles();
            int ner = Integer.parseInt(argument);
            File aaSuppr = contenu[ner - 1];
            String nom = aaSuppr.getName();
            if (aaSuppr.isDirectory()) {
              new Dossier(nom, SystemeFichier.getRepertoireCourantString()).rm();
            } else {
              new Fichier(nom, SystemeFichier.getRepertoireCourantString()).rm();
            }
          }
          break;

        case "copy":
          if (dernierNER != -1) {     // Vérifie si un NER a été spécifié
            File[] contenu = SystemeFichier.getRepertoireCourant().listFiles();
            if (dernierNER > 0 && dernierNER <= contenu.length) {
              File aaCopier = contenu[dernierNER - 1];
              String nom = aaCopier.getName();
              if (aaCopier.isDirectory()) {
                new Dossier(nom, SystemeFichier.getRepertoireCourantString()).copy();
              } else {
                new Fichier(nom, SystemeFichier.getRepertoireCourantString()).copy();
              }
              System.out.println("Element copié: " + nom);
            } else {
              System.out.println("NER invalide.");
            }
          } else {
            System.out.println("Aucun NER spécifié pour la commande copy.");
          }
          break;

        case "paste":
          Path pressePapierDir = Paths.get("Presse-Papier");
          File[] contenu = pressePapierDir.toFile().listFiles();
          File aaCopier = contenu[0];
          if (aaCopier.isDirectory()) {
            Entite entite = new Dossier(SystemeFichier.getRepertoireCourantString());
            entite.paste(SystemeFichier.getRepertoireCourantString());
          } else {
            Entite entite = new Fichier(SystemeFichier.getRepertoireCourantString());
            entite.paste(SystemeFichier.getRepertoireCourantString());
          }
          break;

        case "cut":
          if (dernierNER != -1) {
            File[] contenus = SystemeFichier.getRepertoireCourant().listFiles();
            if (dernierNER > 0 && dernierNER <= contenus.length) {
              File aaCouper = contenus[dernierNER - 1];
              String noms = aaCouper.getName();
              if (aaCouper.isDirectory()) {
                new Dossier(noms, SystemeFichier.getRepertoireCourantString()).cut();
              } else {
                new Fichier(noms, SystemeFichier.getRepertoireCourantString()).cut();
              }
              System.out.println("Element coupé: " + noms);
            } else {
              System.out.println("NER invalide.");
            }
          } else {
            System.out.println("Aucun NER spécifié pour la commande cut.");
          }
          break;

        case "find":  
          if (argument.isEmpty()) {
            System.out.println("Vous n'avez pas spécifié de nom de fichier à rechercher.");
          } else {
            System.out.println(Dossier.find(SystemeFichier.getCheminPath(), argument));
            // Entite.find(SystemeFichier.getCheminPath(), argument);
          }
          break;

        case "+":
          if (dernierNER != -1 && !argument.isEmpty()) {
            annotations.ajoutAnnotation(dernierNER, argument);
          } else {
            System.out.println("NER ou annotation manquante.");
          }
          break;

        case "-":
          if (dernierNER != -1) {
            annotations.suppAnnotation(dernierNER);
          } else {
            System.out.println("NER manquant pour la suppression d'annotation.");
          }
          break;

        case "help": 
          Path cheminH = Paths.get("miniprojet-grp-77_78");
          Path cheminAbsoluH = cheminH.toAbsolutePath().getParent();
          String cheminHelp = cheminAbsoluH.toString().replace("\\", "/")
              + "/ManuelUtilisateur.adoc";
          try (BufferedReader reader = new BufferedReader(new FileReader(cheminHelp))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
              System.out.println(ligne);
            }
          } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
          }
          break;
      
        default:
          System.out.println("Commande non reconnue.");
          break;
      }
      // Met à jour et affiche le contenu du répertoire courant
      SystemeFichier.listerContenuRepertoire();
    }   
  }
}




