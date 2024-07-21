package myVkBot.enums;

import myVkBot.Constants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MethodApi {
    MESSAGE_SEND(Constants.VK_API_METHOD_MESSAGE_SEND);

    private final String methodPath;
}
