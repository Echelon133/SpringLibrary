package ml.echelon133.message;

import java.util.Date;
import java.util.List;

public interface IErrorMessage {
    Date getTimestamp();
    List<String> getMessages();
    String getPath();
    void setTimestamp(Date timestamp);
    void setMessages(List<String> messages);
    void setPath(String path);
    void addSingleMessage(String message);
}
