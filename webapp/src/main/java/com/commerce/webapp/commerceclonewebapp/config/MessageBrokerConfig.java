package com.commerce.webapp.commerceclonewebapp.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.util.Assert;

import javax.jms.*;

@Configuration
public class MessageBrokerConfig implements DestinationResolver {

    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory
                = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("tcp://localhost:61616");
        return activeMQConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setDestinationResolver(destinationResolver());
//        jmsTemplate.setPubSubDomain(true);

        return jmsTemplate;
    }

    @Bean
    public DestinationResolver destinationResolver(){
        return this::resolveDestinationName;
    }

    @Override
    public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
        Assert.notNull(session, "Session must not be null");
        Assert.notNull(destinationName, "Destination name must not be null");
        if (destinationName.endsWith("Topic")) {
            return resolveTopic(session, destinationName);
        }
        else {
            return resolveQueue(session, destinationName);
        }
    }

    Topic resolveTopic(Session session, String topicName) throws JMSException {
        return session.createTopic(topicName);
    }

    Queue resolveQueue(Session session, String queueName) throws JMSException {
        return session.createQueue(queueName);
    }
}

