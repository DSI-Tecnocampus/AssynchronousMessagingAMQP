package cat.tecnocampus.greetingsreceiver.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class AMQPConfiguration {

    @Bean
    public TopicExchange greetingsTopicExchange(@Value("${amqp.exchange.greetings.topic}") final String exchangeTopic) {
        return ExchangeBuilder.topicExchange(exchangeTopic).durable(true).build();
    }

    @Bean
    public Queue consonantsQueue(@Value("${amqp.queue}") final String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding consonantsGreetingBinding(final Queue consonantsQueue,
                                          final TopicExchange greetingsExchange,
                                          @Value("${amqp.exchange.greetings.routing.consonants}") final String routingConsonants){

        return BindingBuilder.bind(consonantsQueue)
                .to(greetingsExchange)
                .with(routingConsonants);
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        final MappingJackson2MessageConverter jsonConverter = new MappingJackson2MessageConverter();

        jsonConverter.getObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        factory.setMessageConverter(jsonConverter);

        return factory;
    }

    @Bean
    public RabbitListenerConfigurer rabbitListenerConfigurer(final MessageHandlerMethodFactory messageHandlerMethodFactory) {
        return (c) -> c.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
    }
}
