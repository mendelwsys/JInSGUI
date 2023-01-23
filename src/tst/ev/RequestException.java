package tst.ev;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 19.01.2023
 * Time: 12:11
 * To change this template use File | Settings | File Templates.
 */
public class RequestException extends Exception {
    private String code = null;
    private String description = "";

    public RequestException() {
    }

    public RequestException(String s) {
        super(s);
    }

    public RequestException(String code, String s) {
        super(s);
        this.code = code;
    }

    public int getAnswerCode() {
        try {
            return Integer.parseInt(this.code);
        } catch (Exception var2) {
            System.out.println("Не удалось распознать код " + this.code);
            return -1;
        }
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullDescription() {
        return this.description;
    }
}
