package at.halora.services.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class DownlinkAckWrapper {
    @JsonProperty("end_device_ids")
    private EndDeviceIds endDeviceIds;
    @JsonProperty("correlation_ids")
    private List<String> correlationIds;
    @JsonProperty("downlink_ack")
    private DownlinkAck downlinkAck;

    @Getter
    public static class EndDeviceIds {
        @JsonProperty("device_id")
        private String deviceId;
        @JsonProperty("application_ids")
        private ApplicationIds applicationIds;
        @JsonProperty("dev_eui")
        private String devEui;
        @JsonProperty("join_eui")
        private String joinEui;
        @JsonProperty("dev_addr")
        private String devAddr;

        @Getter
        public static class ApplicationIds {
            @JsonProperty("application_id")
            private String applicationId;
        }
    }

    @Getter
    public static class DownlinkAck {
        @JsonProperty("session_key_id")
        private String sessionKeyId;
        @JsonProperty("f_port")
        private int fPort;
        @JsonProperty("f_cnt")
        private int fCnt;
        @JsonProperty("frm_payload")
        private String frmPayload;
        private boolean confirmed;
        private String priority;
        @JsonProperty("correlation_ids")
        private List<String> correlationIds;
    }
}
