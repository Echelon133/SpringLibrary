package ml.echelon133.model.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorMessage implements IErrorMessage {
    private Date timestamp;
    private List<String> messages;
    private String path;

    public ErrorMessage() {
        this.messages = new ArrayList<>();
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void addSingleMessage(String message) {
        messages.add(message);
    }
}
