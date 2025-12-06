package edu.supmti;

import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class EventProducer {
    public static void main(String[] args) throws Exception {
        // Vérifier que le topic est fourni comme arg
        if(args.length == 0) {
            System.out.println("Entrer le nom du topic");
            return;
        }
        
        String topicName = args[0].toString(); // lire le topicName fourni comme param
        
        // Accéder aux configurations du producteur
        Properties props = new Properties();
        
        // Spécifier le serveur kafka
        props.put("bootstrap.servers", "localhost:9092");
        
        // Définir un acquittement pour les requêtes du producteur
        props.put("acks", "all");
        
        // Si la requête échoue, le producteur peut réessayer automatiquement
        props.put("retries", 0);
        
        // Spécifier la taille du buffer size dans la config
        props.put("batch.size", 16384);
        
        // Contrôle l'espace total de mémoire disponible au producteur pour le buffering
        props.put("buffer.memory", 33554432);
        
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        
        for(int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<String, String>(topicName, 
                Integer.toString(i), Integer.toString(i)));
        }
        
        System.out.println("Message envoyé avec succès");
        producer.close();
    }
}