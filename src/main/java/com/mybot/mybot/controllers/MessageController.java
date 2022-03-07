package com.mybot.mybot.controllers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class MessageController {

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @EventMapping
    public void handleTextEvent(MessageEvent<TextMessageContent> messageEvent) {
        try {
            /* Retrieve information from the message */
            Source source = messageEvent.getSource();
            String lineId = source.getUserId();
            String replyToken = messageEvent.getReplyToken();
            String message = messageEvent.getMessage().getText();

            /* Get user's display name */
            String displayName = lineMessagingClient
                    .getProfile(lineId)
                    .get()
                    .getDisplayName();

            /* Building the response */
            String answer = String.format("Hello, %s! Your message is %s", displayName, message);
            TextMessage responseMessage = new TextMessage(answer);

            /* Sending the respone */
            lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, responseMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
