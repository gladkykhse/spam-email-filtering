import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.filters.Filter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The Model class represents the machine learning model for spam email filtering.
 * It uses the Weka library for classification and evaluation.
 */
public class Model implements Serializable {

    private final Classifier classifier;
    private final SpamDataset dataset;

    /**
     * Constructs a new Model instance with the specified dataset.
     *
     * @param dataset the dataset used for training and evaluation.
     */
    public Model(SpamDataset dataset) {
        this.classifier = new NaiveBayes();
        this.dataset = dataset;
    }

    /**
     * Loads a previously saved machine learning model from a specified file.
     *
     * @param filePath the path to the file from which the model should be loaded.
     * @return the loaded Model instance.
     * @throws IOException            if an I/O error occurs during the loading process.
     * @throws ClassNotFoundException if the class of the serialized object cannot be found.
     */
    public static Model loadModel(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Model) ois.readObject();
        }
    }

    private Map<String, Double> extractMetrics(Evaluation eval) {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("accuracy", eval.pctCorrect());
        metrics.put("precision", eval.precision(1));
        metrics.put("recall", eval.recall(1));
        metrics.put("f_measure", eval.fMeasure(1));
        metrics.put("area_under_roc_curve", eval.areaUnderROC(1));
        metrics.put("true_positive_rate", eval.truePositiveRate(1));
        metrics.put("false_positive_rate", eval.falsePositiveRate(1));

        return metrics;
    }

    /**
     * Trains the machine learning model using the provided training dataset.
     *
     * @throws Exception if an error occurs during the training process.
     */
    public void train() throws Exception {
        classifier.buildClassifier(dataset.getTrainInstances());
    }

    private Map<String, Double> crossValidateModel(Instances instances) throws Exception {
        Evaluation eval = new Evaluation(instances);
        eval.crossValidateModel(classifier, instances, 5, new Random(42));

        return extractMetrics(eval);
    }

    private Map<String, Double> evaluateTestDataset(Instances instances) throws Exception {
        Evaluation eval = new Evaluation(instances);
        eval.evaluateModel(classifier, instances);

        return extractMetrics(eval);
    }

    /**
     * Evaluates the machine learning model on either the test dataset or performs
     * cross-validation on the training dataset if no test dataset is available.
     *
     * @return a map of evaluation metrics.
     * @throws Exception if an error occurs during the evaluation process.
     */
    public Map<String, Double> evaluate() throws Exception {
        Instances testInstances = dataset.getTestInstances();

        if (testInstances == null) return crossValidateModel(dataset.getTrainInstances());
        else return evaluateTestDataset(testInstances);
    }

    /**
     * Saves the trained machine learning model to a specified file.
     *
     * @param filePath the path to the file where the model should be saved.
     * @throws IOException if an I/O error occurs during the saving process.
     */
    public void saveModel(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }

    /**
     * Predicts whether a given text is spam or ham.
     *
     * @param text the text to be classified.
     * @return "spam" if the text is predicted to be spam, "ham" otherwise.
     * @throws Exception if an error occurs during the prediction process.
     */
    public String predict(String text) throws Exception {
        Attribute targetAttribute = new Attribute("target", (FastVector) null);
        Attribute emailAttribute = new Attribute("email", (FastVector) null);

        FastVector attributes = new FastVector();
        attributes.addElement(targetAttribute);
        attributes.addElement(emailAttribute);

        Instances instances = new Instances("SampleInstances", attributes, 0);
        instances.setClass(targetAttribute);

        DenseInstance instance = new DenseInstance(2);
        instance.setValue(emailAttribute, text);
        instances.add(instance);

        instances = Filter.useFilter(instances, dataset.getFilter());

        double probability;
        try {
            probability = classifier.classifyInstance(instances.getFirst());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (probability < 0.5) return "spam";
        else return "ham";
    }
}
