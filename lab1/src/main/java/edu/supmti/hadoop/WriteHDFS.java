package edu.supmti.hadoop;

import java.io.IOException;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;

public class WriteHDFS {
    public static void main(String[] args) {
        // Vérifier les arguments
        if (args.length != 2) {
            System.err.println("Usage: WriteHDFS <chemin_fichier> <contenu>");
            System.err.println("Exemple: WriteHDFS /user/root/input/bonjour.txt \"Bonjour Hadoop!\"");
            System.exit(1);
        }

        String cheminFichier = args[0];
        String contenu = args[1];

        Configuration conf = new Configuration();
        FileSystem fs = null;
        FSDataOutputStream outStream = null;

        try {
            fs = FileSystem.get(conf);
            Path nomcomplet = new Path(cheminFichier);

            // Vérifier si le fichier existe déjà
            if (fs.exists(nomcomplet)) {
                System.out.println("Attention: Le fichier existe déjà: " + cheminFichier);
                System.out.println("Le fichier sera écrasé...");
                fs.delete(nomcomplet, false);
            }

            // Créer le fichier
            outStream = fs.create(nomcomplet);

            // Écrire le contenu
            outStream.writeUTF("Bonjour tout le monde !");
            outStream.writeUTF(contenu);

            System.out.println("✓ Fichier créé avec succès: " + cheminFichier);
            System.out.println("✓ Contenu écrit:");
            System.out.println("  - Ligne 1: Bonjour tout le monde !");
            System.out.println("  - Ligne 2: " + contenu);

            // Afficher les informations du fichier créé
            FileStatus status = fs.getFileStatus(nomcomplet);
            System.out.println("\nInformations du fichier:");
            System.out.println("  Taille: " + status.getLen() + " bytes");
            System.out.println("  Réplication: " + status.getReplication());
            System.out.println("  Propriétaire: " + status.getOwner());

        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (outStream != null) outStream.close();
                if (fs != null) fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}