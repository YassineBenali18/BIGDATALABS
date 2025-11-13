package edu.supmti.hadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

public class ReadHDFS {
    public static void main(String[] args) {
        // Vérifier les arguments
        if (args.length != 1) {
            System.err.println("Usage: ReadHDFS <chemin_fichier>");
            System.err.println("Exemple: ReadHDFS /user/root/input/purchases.txt");
            System.exit(1);
        }

        String cheminFichier = args[0];
        Configuration conf = new Configuration();
        FileSystem fs = null;
        FSDataInputStream inStream = null;
        BufferedReader br = null;

        try {
            fs = FileSystem.get(conf);
            Path nomcomplet = new Path(cheminFichier);

            // Vérifier si le fichier existe
            if (!fs.exists(nomcomplet)) {
                System.err.println("Erreur: Le fichier n'existe pas: " + cheminFichier);
                System.exit(1);
            }

            // Ouvrir le fichier
            inStream = fs.open(nomcomplet);
            InputStreamReader isr = new InputStreamReader(inStream);
            br = new BufferedReader(isr);

            System.out.println("===== Contenu du fichier: " + cheminFichier + " =====\n");

            // Lire toutes les lignes
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                System.out.println(lineNumber + ": " + line);
                lineNumber++;
            }

            System.out.println("\n===== Fin du fichier (Total: " + (lineNumber - 1) + " lignes) =====");

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermer les ressources dans l'ordre inverse
            try {
                if (br != null) br.close();
                if (inStream != null) inStream.close();
                if (fs != null) fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}