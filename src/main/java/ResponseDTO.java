/**
 * The ResponseDTO class represents the Data Transfer Object (DTO) for holding the prediction result of an email.
 */
public class ResponseDTO {

    private String prediction;

    /**
     * Constructs a new ResponseDTO with the specified email prediction.
     *
     * @param prediction the predicted status of the email ("spam" or "ham").
     */
    public ResponseDTO(String prediction) {
        this.prediction = prediction;
    }

    /**
     * Gets the predicted status of the email.
     *
     * @return the predicted status ("spam" or "ham").
     */
    public String getEmailStatus() {
        return prediction;
    }

    /**
     * Sets the predicted status of the email.
     *
     * @param emailStatus the predicted status to set ("spam" or "ham").
     */
    public void setEmailStatus(String emailStatus) {
        this.prediction = emailStatus;
    }
}
