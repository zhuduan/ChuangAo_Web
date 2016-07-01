package ChuangAo.WebSite.javaconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
	
	@Value("${sockjs.setDisconnectDelay:20000}")
    int sockjs_setDisconnectDelay;

    @Value("${sockjs.setHeartbeatTime:25000}")
    int sockjs_setHeartbeatTime;

    // WebSocketTransport settings
    @Value("${WebSocketTransportRegistration.MessageSizeLimit:131072}")
    int MessageSizeLimit;

    @Value("${WebSocketTransportRegistration.SendTimeLimit:15000}")
    int SendTimeLimit;

    @Value("${WebSocketTransportRegistration.SendBufferSizeLimit:524288}")
    int SendBufferSizeLimit;

    // ClientOutboundChannel configs
    @Value("${ClientOutboundChannel.corePoolSize:25}")
    int ClientOutboundChannelcorePoolSize;
    @Value("${ClientOutboundChannel.maxPoolSize:200}")
    int ClientOutboundChannelmaxPoolSize;

    // ClientInboundChannel configs
    @Value("${ClientInboundChannel.corePoolSize:25}")
    int ClientInboundChannelcorePoolSize;
    @Value("${ClientInboundChannel.maxPoolSize:200}")
    int ClientInboundChannelmaxPoolSize;
    
	
	@Override    
    public void registerStompEndpoints(StompEndpointRegistry registry) {        
        registry.addEndpoint("/websocket").withSockJS()
        .setDisconnectDelay(sockjs_setDisconnectDelay)
        .setHeartbeatTime(sockjs_setHeartbeatTime)
        .setWebSocketEnabled(true);
    }    
	
    @Override    
    public void configureMessageBroker(MessageBrokerRegistry registry) {  
        registry.enableSimpleBroker("/topic","/queue");    //real broker may should use only topic or queue  
        registry.setApplicationDestinationPrefixes("/app"); 
    }
    
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer()
    {
        ServletServerContainerFactoryBean container = new  ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        container.setAsyncSendTimeout(5000);
        container.setMaxSessionIdleTimeout(3600*1000);
        return container;
    }
    
    /**
     * Configure the {@link org.springframework.messaging.MessageChannel} used
     * for outgoing messages to WebSocket clients. By default the channel is
     * backed by a thread pool of size 1. It is recommended to customize thread
     * pool settings for production use.
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration)
    {
        registration.taskExecutor().corePoolSize(ClientOutboundChannelcorePoolSize).maxPoolSize(ClientOutboundChannelmaxPoolSize);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration)
    {
        registration.taskExecutor().corePoolSize(ClientInboundChannelcorePoolSize).maxPoolSize(ClientInboundChannelmaxPoolSize);
    }
	
}

