import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.stream.Collectors;

/**
 * The MyHandler class implements the HttpHandler interface and handles HTTP requests for email prediction.
 */
public class MyHandler implements HttpHandler {

    private final Model model;
    private final Gson gson = new Gson();

    /**
     * Constructs a new MyHandler instance with the specified machine learning model.
     *
     * @param model the machine learning model used for email prediction.
     */
    public MyHandler(Model model) {
        this.model = model;
    }

    /**
     * Handles an HTTP request for email prediction.
     *
     * @param exchange the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs during request processing.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Read the request body
        InputStream requestBody = exchange.getRequestBody();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(requestBody));
        String body = bufferedReader.lines().collect(Collectors.joining("\n"));

        // Parse the request body into an EmailDTO object
        EmailDTO inputEmail = gson.fromJson(body, EmailDTO.class);

        // Perform email prediction using the machine learning model
        String prediction = null;
        try {
            prediction = model.predict(inputEmail.getEmail());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Create a response DTO with the prediction
        String response = gson.toJson(new ResponseDTO(prediction));

        // Send the HTTP response
        exchange.sendResponseHeaders(200, response.length());

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
