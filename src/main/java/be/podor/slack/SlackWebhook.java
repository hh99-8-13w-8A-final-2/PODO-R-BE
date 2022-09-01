package be.podor.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlackWebhook {

    @Value("${slack.token}")
    private String slackToken;

    @Value("${slack.channel.monitor}")
    private String slackChannel;

    public void postSlackMessage(String message) {
        try {
            MethodsClient methods = Slack.getInstance().methods(slackToken);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(slackChannel)
                    .text(message)
                    .build();

            methods.chatPostMessage(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
