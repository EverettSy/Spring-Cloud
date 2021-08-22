package com.syraven.cloud.config;

import com.syraven.cloud.utlis.SnowFlake;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 上下文管理，初始化一些需要的bean
 * @author syrobin
 */

@Component
public class ServerInitConfiguration implements ApplicationListener<WebServerInitializedEvent> {


    private int serverPort;


    public String getUrl(){
        InetAddress address = null;

        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        assert address != null;
        return "http://"+address.getHostAddress()+":"+this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {

        this.serverPort = event.getWebServer().getPort();
    }

    @Bean("idGenerator")
    public SnowFlake snowFlake(){
        return new SnowFlake(219);
    }


}
