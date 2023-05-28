package at.halora.services.mqtt;

import at.halora.messagelogic.IMessageLogic;
import at.halora.messagelogic.Message;
import at.halora.services.IMessagingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;

import java.util.*;

public class TTNHandler implements IMessagingService {

    private IMessageLogic messageLogic;

    private final Properties config;

    private final ObjectMapper objectMapper;

    private IMqttClient mqttClient;

    private String publisherId;

    public TTNHandler(IMessageLogic messageLogic, Properties configProperties) {
        this.messageLogic = messageLogic;
        this.config = configProperties;
        publisherId = UUID.randomUUID().toString();
        objectMapper = new ObjectMapper();
        try {

            mqttClient = new MqttClient(config.getProperty("MQTT_URI"), publisherId);

            var options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setPassword(config.getProperty("MQTT_API_KEY").toCharArray()); //Add API Key here
            options.setUserName(config.getProperty("MQTT_USER")); //applicationId@tenantId

            mqttClient.connect(options);
        } catch (Exception e) {
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

            //to do: subscribe to all device topics available
            mqttClient.subscribe(config.getProperty("MQTT_TOPIC"), this::handleUplink);

            while (true); //wait forever
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleUplink(String topic, MqttMessage msg) {
        byte[] payload = msg.getPayload();
        String json = new String(payload);
        System.out.println(json);
        try {
            JsonNode uplinkMessage = objectMapper.readTree(json).path("uplink_message");
            JsonNode device_id = objectMapper.readTree(json).path("end_device_ids").path("device_id");
            JsonNode frm_payload = uplinkMessage.path("frm_payload");
            JsonNode timestamp = objectMapper.readTree(json).path("received_at");

            byte[] decoded_message = Base64.getDecoder().decode(frm_payload.asText());
            String decoded_string = new String(decoded_message);

            String recipient = decoded_string.split(";")[0];
            String message = decoded_string.split(";")[1];

            Message msgObj = new Message(timestamp.asText().split("\\.")[0], device_id.asText(), recipient, message);


            messageLogic.sendMessage(msgObj);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void pushDownlink(String message, String deviceId) {

        //v3/{application id}@{tenant id}/devices/{device id}/down/push
        final String TOPIC_PUSH = String.format("v3/%s/devices/%s/down/push", config.getProperty("MQTT_USER"), deviceId);
        final String TOPIC_ACK = String.format("v3/%s/devices/%s/down/ack", config.getProperty("MQTT_USER"), deviceId);

        //create downlink object to publish
        var downlink = new DownlinkPushWrapper.Downlink();
        downlink.setPriority("NORMAL");
        downlink.setFPort(15);
        downlink.setFrmPayload(Base64.getEncoder().encodeToString(message.getBytes()));
        var downlinkWrapper = new DownlinkPushWrapper();
        downlinkWrapper.addDownlink(downlink);

        //publish downlink
        var m = new MqttMessage();
        try {
            m.setPayload(objectMapper.writeValueAsBytes(downlinkWrapper));
            mqttClient.publish(TOPIC_PUSH, m);
        } catch (JsonProcessingException | MqttException e) {
            e.printStackTrace();
        }

        //subscribe to response
        try {
            mqttClient.subscribe(TOPIC_ACK, (topic, msg) -> {
                var res = objectMapper.readValue(msg.getPayload(), DownlinkAckWrapper.class);
                System.out.println("Payload: " + res.getDownlinkAck().getFrmPayload() + " to " + res.getEndDeviceIds().getDeviceId() + "acknowledged!");
            });
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
