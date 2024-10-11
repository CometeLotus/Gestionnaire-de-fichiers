package fr.uvsq.cprog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
* Constructeur de la classe Annotation.
*/
public class Annotation {
  public Map<String, String> annotations;
  private Gson gson;
  
  
  /**
  * Constructeur de la classe Annotation.
  * Initialise une nouvelle instance de la classe avec une instance Gson
  * et une HashMap vide pour les annotations.
  * Charge également les annotations à partir du fichier spécifié.
  */
  public Annotation() {
    this.gson = new Gson();
    this.annotations = new HashMap<>();
    chargementAnnotations();
  }

  private String getNotesFilePath() {
    // Construit dynamiquement le chemin vers le fichier "notes.json" dans le répertoire courant
    return SystemeFichier.getRepertoireCourant().getAbsolutePath() + File.separator + "notes.json";
  }

  /**
  * Charge les annotations à partir du fichier spécifié.
  * Si le fichier existe, les données sont lues et désérialisées en utilisant Gson,
  * puis stockées dans la variable 'annotations'.
  * Si le fichier n'existe pas ou s'il y a une erreur lors de la lecture/désérialisation,
  * une nouvelle instance de HashMap est créée pour 'annotations'.
  */
  public void chargementAnnotations() {
    Path path = Paths.get(getNotesFilePath());
    try {
      if (!Files.exists(path)) {
        // Si le fichier n'existe pas, créez-le et initialisez avec une HashMap vide
        Files.createFile(path);
        this.annotations = new HashMap<>();
        // Sauvegarde immédiatement le fichier JSON vide pour éviter des erreurs de lecture
        saveAnnotations();
      } else {
        // Lecture du contenu existant de notes.json
        String json = new String(Files.readAllBytes(path));
        if (!json.isEmpty()) {
          TypeToken<Map<String, String>> typeToken = new TypeToken<>() {};
          this.annotations = new Gson().fromJson(json, typeToken.getType());
        } else {
          this.annotations = new HashMap<>();
        }
      }
    } catch (IOException e) {
      System.out.println("Erreur lors de l'accès à " + path + ": " + e.getMessage());
      // Assurez-vous d'avoir toujours une map initialisée même en cas d'erreur
      this.annotations = new HashMap<>();
    }
  }


  /**
  * Ajoute une annotation à un fichier spécifié par son index.
  * L'index doit être supérieur à zéro et inférieur ou égal au nombre de fichiers présents.
  * Si l'index est valide, l'annotation spécifiée est ajoutée au fichier correspondant.
  * Si une annotation existe déjà pour ce fichier, la nouvelle annotation est
  * concaténée à la précédente.
  * Après l'ajout de l'annotation, les annotations sont sauvegardées dans le fichier spécifié.
  *
  * @param ner L'index du fichier auquel ajouter l'annotation (commence à 1).
  * @param annotation La nouvelle annotation à ajouter.
  */
  public void ajoutAnnotation(int ner, String annotation) {
    File[] contenu = SystemeFichier.repertoireCourant.listFiles();
    if (contenu != null && ner > 0 && ner <= contenu.length) {
      File element = contenu[ner - 1];
      String chemin = element.getPath();

      // Récupérer la valeur actuelle associée à la clé
      String valeurActuelle = annotations.get(chemin);

      // Concaténer la nouvelle annotation à la valeur actuelle, si elle existe
      if (valeurActuelle != null) {
        annotations.put(chemin, valeurActuelle + annotation);
      } else {
        // Sinon, ajouter simplement la nouvelle annotation
        annotations.put(chemin, annotation);
      }
    }
    saveAnnotations();
  }

  /**
  * Supprime une annotation correspondant à l'index spécifié.
  * L'index doit être supérieur à zéro et inférieur ou égal au nombre d'annotations présentes.
  * Si l'index est valide, l'annotation correspondante est supprimée.
  * Après la suppression, les annotations sont sauvegardées dans le fichier spécifié.
  *
  * @param ner L'index de l'annotation à supprimer (commence à 1).
  */
  public void suppAnnotation(int ner) {
    File[] contenu = SystemeFichier.repertoireCourant.listFiles();
    if (contenu != null && ner > 0 && ner <= contenu.length) {
      File element = contenu[ner - 1];
      String chemin = element.getPath();
      annotations.remove(chemin);
    }
    saveAnnotations();
  }

  /**
  * Sauvegarde les annotations dans un fichier au format JSON.
  * Les annotations sont converties en JSON à l'aide de la bibliothèque Gson,
  * puis écrites dans le fichier spécifié par FILE_PATH.
  * En cas d'erreur lors de la sauvegarde, l'exception est gérée sans propagation.
  * Les détails de l'exception peuvent être consultés dans les logs.
  */
  public void saveAnnotations() {
    try {
      String json = gson.toJson(annotations);
      Files.write(Paths.get(getNotesFilePath()), json.getBytes()); 
    } catch (Exception e) {
      // Gérer l'exception
    }
  }


  /**
  * Supprime l'annotation associée à un chemin spécifié, s'il existe.
  *
  * @param chemin Le chemin du fichier ou du dossier pour lequel supprimer l'annotation.
  */
  public void supprimerAnnotationParChemin(String chemin) {
    if (annotations.containsKey(chemin)) {
      annotations.remove(chemin);
      saveAnnotations(); // Sauvegarde les changements
    }
  }


  //Nous permet de récuperer l'annotation avec le chemin du fichier
  public Optional<String> getAnnotation(String chemin) {
    return Optional.ofNullable(annotations.get(chemin));
  }
}