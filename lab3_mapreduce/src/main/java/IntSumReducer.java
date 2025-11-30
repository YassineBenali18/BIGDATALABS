package edu.ismagi.hadoop.mapreduce;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
    private IntWritable result = new IntWritable();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        
        int sum = 0;
        
        // Additionner toutes les occurrences du mot
        for (IntWritable val : values) {
            sum += val.get();
        }
        
        result.set(sum);
        
        // Émettre le résultat final (mot, nombre total d'occurrences)
        context.write(key, result);
    }
}