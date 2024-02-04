import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.NoSuchFileException;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * The Main class serves as the entry point for the spam email filtering application.
 */
public class Main {

    /**
     * The main method for running the spam email filtering application.
     *
     * @param args command line arguments.
     *             For API mode: modelPath port
     *             For Training mode: trainDataPath testDataPath modelPath
     * @throws Exception if an error occurs during application execution.
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            // API mode
            String modelPath = args[0];
            if (!(new File(modelPath)).exists())
                throw new NoSuchFileException("Model file does not exist. Train model first and double check file path argument");
            int port = Integer.parseInt(args[1]);

            Model model = Model.loadModel(modelPath);

            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/email", new MyHandler(model));
            server.setExecutor(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
            server.start();
        } else if (args.length == 3) {
            // Training mode
            String trainDataPath = args[0];
            if (!(new File(trainDataPath)).exists())
                throw new NoSuchFileException("Train data file does not exist. Double check file path argument and existence of this file");
            String testDataPath = args[1];
            if (!(new File(testDataPath)).exists())
                throw new NoSuchFileException("Test data file does not exist. Double check file path argument and existence of this file");
            String modelPath = args[2];

            SpamDataset dataset = new SpamDataset(trainDataPath, testDataPath);
            Model model = new Model(dataset);
            model.train();
            Map<String, Double> map = model.evaluate();
            Utils.printMetrics(map);

            model.saveModel(modelPath);
        } else {
            throw new IllegalArgumentException("Invalid number of arguments provided");
        }
    }
}
