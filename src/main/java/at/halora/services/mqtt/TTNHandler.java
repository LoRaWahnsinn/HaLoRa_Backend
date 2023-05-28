package at.halora.services.mqtt;

import at.halora.messagelogic.IMessageLogic;
import at.halora.services.IMessagingService;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Properties;
import java.util.UUID;

public class TTNHandler implements IMessagingService {

    private IMessageLogic messageLogic;

    private final Properties config;

    private IMqttClient mqttClient;

    private String publisherId;

    public TTNHandler(IMessageLogic messageLogic, Properties configProperties) {
        this.messageLogic = messageLogic;
        this.config = configProperties;
        publisherId = UUID.randomUUID().toString();
        try {
            //use local mosquitto broker for demonstration
            mqttClient = new MqttClient(config.getProperty("MQTT_URI"), publisherId);

            var options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setPassword(config.getProperty("MQTT_API_KEY").toCharArray()); //Add API Key here
            options.setUserName(config.getProperty("MQTT_USER")); //applicationId@tenantId

            mqttClient.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean sendMessage(String id, String message) {
        return true;
    }

    @Override
    public void run() {
        try {
            mqttClient.subscribe(config.getProperty("MQTT_TOPIC"), (topic, msg) -> {
                byte[] payload = msg.getPayload();

                String bytes = new String(payload);
                System.out.println(bytes);
            });

            while (true); //wait forever
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
