package helpers;

import android.content.Context;

import net.igenius.mqttservice.MQTTService;
import net.igenius.mqttservice.MQTTServiceCommand;


import java.util.UUID;

/**
 * Created by wildan on 3/19/2017.
 */

public class MqttHelper {

    String server = "ssl://m15.cloudmqtt.com:25784";
    String username = "bbnfqqdt";
    String password = "J_yF2f1oebbK";
    String clientId = UUID.randomUUID().toString();
    String publishTopic = "/some/publish/topic";
    String subscribeTopic = "/some/subscribe/topic";

    public MqttHelper(Context context){
        MQTTService.NAMESPACE = "com.cahjaya.lian.greenhousecabai"; //or BuildConfig.APPLICATION_ID;
        MQTTService.KEEP_ALIVE_INTERVAL = 60; //in seconds
        MQTTService.CONNECT_TIMEOUT = 30; //in seconds
        MQTTServiceCommand.connect(context, server, clientId, username, password);
    }
}
