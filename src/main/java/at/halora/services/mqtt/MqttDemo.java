package at.halora.services.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MqttDemo {

    private String publisherId;

    private IMqttClient mqttClient;

    private enum Topics {
        UPLINK("mqtt/uplink"),
        DOWNLINK("mqtt/downlink");
        private String topic;

        Topics(String topic) {
            this.topic = topic;
        }
    }

    public MqttDemo() {
        publisherId = UUID.randomUUID().toString();
        try {
            //use local mosquitto broker for demonstration
            mqttClient = new MqttClient("tcp://localhost:1883", publisherId);

            var options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            mqttClient.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void receiveMessage() {

        CountDownLatch receivedSignal = new CountDownLatch(60);
        try {
            mqttClient.subscribe(Topics.UPLINK.topic, (topic, msg) -> {
                byte[] payload = msg.getPayload();

                String bytes = new String(payload);
                System.out.println(bytes);
                receivedSignal.countDown();
            });

            receivedSignal.await(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
