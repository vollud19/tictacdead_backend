package at.kaindorf.tictacdead.ws.config;

import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Setter
@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin(origins = "*")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/player");
        config.setApplicationDestinationPrefixes("/app");
    }

//    USE the  IP-Address of the React Server
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connections").setAllowedOrigins("http://192.168.82.182:3000/","http://localhost:3000/").withSockJS();
        registry.addEndpoint("/player1").setAllowedOrigins("http://192.168.82.182:3000/","http://localhost:3000/").withSockJS();
//        registry.addEndpoint("/player2").setAllowedOrigins("http://192.168.253.182:3000/").withSockJS();
    }

}
