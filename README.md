# Spam Email Filtering

Welcome to the Spam Email Filtering project! This application, equipped with an API, efficiently detects whether a provided email is spam or not.

## General information
The core of the project leverages the `Weka` library due to its user-friendly nature and convenient tools for data processing. I opted for the Naive Bayes classifier in conjunction with the TF-IDF vectorizer. For this project, I manually implemented the data preprocessing step, utilizing a small subset of the Enron-Spam dataset, which contains unprocessed email contents and class labels.

The web component of the project employs the built-in `httpserver` library for its simplicity and a user-friendly interface for creating server applications. Additionally, I integrated the `Gson` library to handle requests and responses in JSON format.

## Project files
- `src/main/java/Main.java` - The entry point of the application, featuring two modes: model training and API operation.
- `src/main/java/SpamDataset.java` - Class containing essential methods for data loading, preprocessing (e.g., vectorization), dataset creation, and the filter used in the model for predicting new data.
- `src/main/java/Model.java` - Class representing a model, facilitating training, evaluation, prediction, etc.
- `src/main/java/Utils.java` - Class containing two utilities (CSV to ARFF converter and a metrics map printer) useful during project development.
- `src/main/java/EmailDTO.java` - Class representing a Data Transfer Object (DTO) for holding email information.
- `src/main/java/ResponseDTO.java` - Class representing a Data Transfer Object (DTO) for holding response information.
- `src/main/java/MyHandler.java` - Class implementing the HttpHandler interface to handle HTTP requests for email prediction.
- `src/main/java/CustomExceptions.java` - Class containing a custom exception used in the file verification part of the SpamDataset class.

- `src/main/resources/model.ser` - Serialized model enabling prediction on new data without retraining.
- `src/main/resources/train_data.arff` - Training data in ARFF format (commonly used format in the Weka library).
- `src/main/resources/test_data.arff` - Testing data for the model.

- `pom.xml` - Maven project configuration file.

## User Guide
The application has two execution modes:

First of all run in the project root directorty:
```text
mvn compile
```

### Training model
```shell
mvn exec:java@train
```
This command triggers the training pipeline, displaying metrics on the test data, and saving the model to the specified file (third argument). The first argument is the path to the training data, and the second one is the path to the testing data.
### Running API server application
```shell
mvn exec:java@api
````
This command launches the server application (API). The first argument is the path to the saved model, and the second one is the port on which you want to run the application. To test the API, execute the following command in a separate terminal:
```shell
curl -X POST http://localhost:1234/email -H "Content-Type: application/json" -d '{"email": "here is some contents of your email"}'
```
Output:
```text
{"prediction":"ham"}
```

## Additional notes
Despite the commendable metrics of the models, real-world performance may vary. The TF-IDF vectorizer combined with the Naive Bayes model has limitations, and the vectorizer's word capacity can impact quality.