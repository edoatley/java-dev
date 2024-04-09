package uk.org.edoatley.servlet.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Greeting {
    private String message;

    public Greeting() {
        super();
    }

    public Greeting(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
