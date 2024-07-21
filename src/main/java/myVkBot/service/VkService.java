package myVkBot.service;

import myVkBot.ApplicationConfiguration;
import myVkBot.entites.Event;
import myVkBot.enums.CallbackApi;
import myVkBot.enums.MethodApi;
import myVkBot.response.TestResponse;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VkService {
    private static final Logger LOG = Logger.getLogger(VkService.class);
    private final ApplicationConfiguration config;

    public String doResponse(Event event) {
        if (!event.getSecret().equals(config.getSecretKey())) {
            LOG.error("Received secret key does not match the one specified in " +
                    "the application.yaml configuration: " + event.getSecret());

            return "error";
        }

        LOG.debug("Received: " + event.toString());

        if (event.getType() == CallbackApi.CONFIRMATION) {
            return config.getConfirmationToken();
        } else if (event.getType() == CallbackApi.MESSAGE_NEW) {
            new TestResponse(event, config.accessToken).processResponse(MethodApi.MESSAGE_SEND);
        }
        return "ok";
    }
}
