package myVkBot.entites;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import myVkBot.Constants;
import myVkBot.enums.CallbackApi;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Event {
    @JsonProperty(Constants.EVENT_TYPE)
    private CallbackApi type;

    @JsonProperty(Constants.EVENT_GROUP_ID)
    private Long groupId;

    @JsonProperty(Constants.EVENT_ID)
    private String eventId;

    @JsonProperty(Constants.EVENT_SECRET)
    private String secret;

    @JsonValue
    @JsonProperty(Constants.EVENT_OBJECT)
    private EventObject eventObject;

    @Override
    public String toString() {
        String message;

        if (type == CallbackApi.MESSAGE_NEW) {
            message = "type: '" + type.name()
                    + "', text: '" + eventObject.getText()
                    + "', from user_id: " + eventObject.getUserId();
        } else if (type == CallbackApi.MESSAGE_REPLY) {
            message = "type: '" + type.name()
                    + "', text: '" + eventObject.getText()
                    + "', to user_id: " + eventObject.getUserId();
        } else {
            message = "type: '" + type.name();
        }

        return message;
    }
}
