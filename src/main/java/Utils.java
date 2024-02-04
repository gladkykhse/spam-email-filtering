import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * The Utils class provides utility methods for common tasks in the spam email filtering project.
 */
public class Utils {

    /**
     * Converts a CSV file to an ARFF file.
     *
     * @param source      the path to the source CSV file.
     * @param destination the path to save the resulting ARFF file.
     * @throws IOException if an I/O error occurs during the conversion.
     */
    public static void csvToArff(String source, String destination) throws IOException {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(source));
        Instances data = loader.getDataSet();

        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(destination));
        saver.writeBatch();
    }

    /**
     * Prints the metrics from a map to the console.
     *
     * @param map the map containing metric names and corresponding values.
     */
    public static void printMetrics(Map<String, Double> map) {
        if (map == null || map.isEmpty()) {
            System.out.println("Metrics map is empty or null.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Metrics:\n");

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            sb.append(String.format("%-20s : %.2f\n", entry.getKey(), entry.getValue()));
        }

        System.out.println(sb);
    }
}
