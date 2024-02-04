import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stopwords.Rainbow;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.Serializable;

/**
 * The SpamDataset class represents a dataset used for spam email filtering.
 * It includes methods to read and preprocess datasets for training and testing.
 */
public class SpamDataset implements Serializable {

    private final int classColId;
    private final int words2keep;

    private final StringToWordVector filter;
    private final Instances trainInstances;
    private final Instances testInstances;

    /**
     * Constructs a SpamDataset instance using the specified training data file path.
     *
     * @param trainDataPath the path to the training data file in ARFF format.
     * @throws Exception if an error occurs during dataset initialization.
     */
    public SpamDataset(String trainDataPath) throws Exception {
        checkFileValidity(trainDataPath);

        this.classColId = 0;
        this.words2keep = 1000;

        filter = new StringToWordVector();

        setupFilter(getInstances(trainDataPath));
        trainInstances = Filter.useFilter(getInstances(trainDataPath), filter);
        testInstances = null;
    }

    /**
     * Constructs a SpamDataset instance using the specified training and test data file paths.
     *
     * @param trainDataPath the path to the training data file in ARFF format.
     * @param testDataPath  the path to the test data file in ARFF format.
     * @throws Exception if an error occurs during dataset initialization.
     */
    public SpamDataset(String trainDataPath, String testDataPath) throws Exception {
        checkFileValidity(trainDataPath);
        checkFileValidity(testDataPath);

        this.classColId = 0;
        this.words2keep = 1000;

        filter = new StringToWordVector();

        setupFilter(getInstances(trainDataPath));
        trainInstances = Filter.useFilter(getInstances(trainDataPath), filter);
        testInstances = Filter.useFilter(getInstances(testDataPath), filter);
    }

    /**
     * Constructs a SpamDataset instance using the specified training and test data file paths,
     * class column index, and number of words to keep during string-to-word vector conversion.
     *
     * @param trainDataPath the path to the training data file in ARFF format.
     * @param testDataPath  the path to the test data file in ARFF format.
     * @param classColId    the index of the class column.
     * @param words2keep    the number of words to keep during string-to-word vector conversion.
     * @throws Exception if an error occurs during dataset initialization.
     */
    public SpamDataset(String trainDataPath, String testDataPath, int classColId, int words2keep) throws Exception {
        checkFileValidity(trainDataPath);
        checkFileValidity(testDataPath);

        this.classColId = classColId;
        this.words2keep = words2keep;

        filter = new StringToWordVector();

        setupFilter(getInstances(trainDataPath));
        trainInstances = Filter.useFilter(getInstances(trainDataPath), filter);
        testInstances = Filter.useFilter(getInstances(testDataPath), filter);
    }

    private int extractExtensionStartIndex(String filePath) {
        return filePath.lastIndexOf('.');
    }

    private void checkFileValidity(String datasetFilePath) throws CustomExceptions.InvalidFileFormatException {
        int index = extractExtensionStartIndex(datasetFilePath);
        if (index <= 0 || !datasetFilePath.substring(index + 1).equalsIgnoreCase("arff"))
            throw new CustomExceptions.InvalidFileFormatException("File " + datasetFilePath + " has an invalid extension. Expected .arff file");
    }

    private Instances getInstances(String dataPath) {
        try {
            Instances datasetInstances = ConverterUtils.DataSource.read(dataPath);
            datasetInstances.setClassIndex(classColId);

            return datasetInstances;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the training instances after preprocessing.
     *
     * @return the preprocessed training instances.
     */
    public Instances getTrainInstances() {
        return trainInstances;
    }

    /**
     * Gets the test instances after preprocessing.
     *
     * @return the preprocessed test instances, or null if no test instances are available.
     */
    public Instances getTestInstances() {
        return testInstances;
    }

    /**
     * Gets the StringToWordVector filter used for preprocessing.
     *
     * @return the StringToWordVector filter.
     */
    public StringToWordVector getFilter() {
        return filter;
    }

    private void setupFilter(Instances dataset) {
        try {
            filter.setInputFormat(dataset);
            filter.setIDFTransform(true);
            filter.setStopwordsHandler(new Rainbow());
            filter.setLowerCaseTokens(true);
            filter.setStemmer(new LovinsStemmer());
            filter.setWordsToKeep(words2keep);
            filter.setInputFormat(dataset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
