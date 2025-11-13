package edu.supmti.hadoop;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

public class HadoopFileStatus {
    public static void main(String[] args) {
        // Vérifier les arguments
        if (args.length != 3) {
            System.err.println("Usage: HadoopFileStatus <chemin> <nom_fichier> <nouveau_nom>");
            System.err.println("Exemple: HadoopFileStatus /user/root/input purchases.txt achats.txt");
            System.exit(1);
        }

        String chemin = args[0];
        String nomFichier = args[1];
        String nouveauNom = args[2];

        Configuration conf = new Configuration();
        FileSystem fs = null;

        try {
            fs = FileSystem.get(conf);
            Path filepath = new Path(chemin, nomFichier);

            // Vérifier si le fichier existe
            if (!fs.exists(filepath)) {
                System.out.println("Le fichier n'existe pas: " + filepath);
                System.exit(1);
            }

            // Récupérer les informations du fichier
            FileStatus status = fs.getFileStatus(filepath);

            // Afficher les informations
            System.out.println("===== Informations du fichier =====");
            System.out.println("Nom du fichier: " + filepath.getName());
            System.out.println("Taille: " + status.getLen() + " bytes");
            System.out.println("Propriétaire: " + status.getOwner());
            System.out.println("Permissions: " + status.getPermission());
            System.out.println("Réplication: " + status.getReplication());
            System.out.println("Taille de bloc: " + status.getBlockSize() + " bytes");

            // Informations sur les blocs
            System.out.println("\n===== Localisation des blocs =====");
            BlockLocation[] blockLocations = fs.getFileBlockLocations(status, 0, status.getLen());
            
            for (int i = 0; i < blockLocations.length; i++) {
                BlockLocation blockLocation = blockLocations[i];
                String[] hosts = blockLocation.getHosts();
                
                System.out.println("\nBloc " + (i + 1) + ":");
                System.out.println("  Offset: " + blockLocation.getOffset());
                System.out.println("  Longueur: " + blockLocation.getLength());
                System.out.print("  Hôtes: ");
                for (String host : hosts) {
                    System.out.print(host + " ");
                }
                System.out.println();
            }

            // Renommer le fichier
            Path nouveauPath = new Path(chemin, nouveauNom);
            boolean success = fs.rename(filepath, nouveauPath);
            
            if (success) {
                System.out.println("\n✓ Fichier renommé avec succès: " + nomFichier + " → " + nouveauNom);
            } else {
                System.out.println("\n✗ Échec du renommage du fichier");
            }

        } catch (IOException e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}