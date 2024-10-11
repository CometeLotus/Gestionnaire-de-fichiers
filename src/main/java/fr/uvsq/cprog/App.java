package fr.uvsq.cprog;


/**
* Classe principale de l'application.
* Cette classe contient la méthode principale (main) qui sert de point d'entrée à l'application.
* Elle lance l'exécution de l'interface utilisateur ou d'autres composants de l'application.
*/
public class App {

  /**
  * Point d'entrée de l'application.
  * Cette méthode lance l'interface utilisateur de la classe
  * Command en passant les arguments spécifiés.
  * Elle gère également les exceptions pouvant survenir pendant
  * l'exécution en affichant la trace de la pile.
  *
  * @param args Les arguments de la ligne de commande passés au programme.
  */
  public static void main(String[] args) {
    try {
      // Lancement de l'interface utilisateur de la classe Command
      Command.main(args);
    } catch (Exception e) {
      // Gestion des exceptions pouvant survenir durant l'exécution
      e.printStackTrace();
    }
  }
}
