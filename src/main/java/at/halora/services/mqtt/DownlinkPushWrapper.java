package at.halora.services.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class DownlinkPushWrapper {

    @Getter
    @JsonProperty("downlinks")
    private List<Downlink> downlinks = new ArrayList<>();

    public void addDownlink(Downlink downlink) {
        this.downlinks.add(downlink);
    }

    @Setter
    @Getter
    public static class Downlink {
        @JsonProperty("f_port")
        private int fPort;
        @JsonProperty("frm_payload")
        private String frmPayload;
        private String priority;
    }
}
