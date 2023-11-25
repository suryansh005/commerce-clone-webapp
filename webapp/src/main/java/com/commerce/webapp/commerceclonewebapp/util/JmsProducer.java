package com.commerce.webapp.commerceclonewebapp.util;

import com.commerce.webapp.commerceclonewebapp.model.jms.JobMessage;
import com.commerce.webapp.commerceclonewebapp.model.jms.JobType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

@Component
public class JmsProducer {

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(String destinationName, JobMessage jobMessage) throws Exception {
        try {
//            jmsTemplate.convertAndSend(destinationName, jobMessage);

            jmsTemplate.send(destinationName, messageCreator -> {
                TextMessage message = messageCreator.createTextMessage();
                message.setText(jobMessage.toJson());
                return message;
            });

//            jmsTemplate.convertAndSend("webapp-notification", "Hi");
        } catch (Exception  ex){
            System.out.println(ex);
            throw ex;
        }
    }
}
