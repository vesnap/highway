package info.kovanovic.camelclojure;

import java.util.List;

import org.apache.camel.Message;
import org.apache.camel.Exchange;

public interface AbstractSplitter {

    List split(String body, Message message, Exchange exchange);
}
