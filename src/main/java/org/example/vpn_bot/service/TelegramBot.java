package org.example.vpn_bot.service;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.vpn_bot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.DefaultAbsSender;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @PostConstruct
    private void init() {

    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long id = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    //registerUser(update);
                    startCommand(update);
                    break;
                case "/search":
                    search(update);
                    break;
                case "/category":
                    category(update);
                    break;
                case "/history":
                    history(update);
                    break;
                case "/help":
                    sendMessage(update.getMessage().getChatId(), "/search если хотите найти самое выгодное предложние на ваш товар\n" +
                            "/category вы выбираете категорию в которой будет искаться ваш товар\n" +
                            "/history История ваших запросов\n");
                    break;
                case "/info":
                    sendMessage(update.getMessage().getChatId(), "Разработчик бота @ezz_887\n Бот создан для мониторинга," +
                            " самых выгодных предложений на выбранный товар");
                    break;
                default:
                    sendMessage(update.getMessage().getChatId(), "Привет 111");
                    break;
            }
        }
    }
    private void startCommand(Update update){
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setParseMode("HTML");
        message.setText("<b>Добро пожаловать в бот!</b> \n" + "\nЧтобы получить доступ, нажми кнопку ниже:");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> but = new ArrayList<>();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("✅ Получить доступ ✅");
        button.setCallbackData("ACCEPT");
        but.add(button);
        buttons.add(but);
        keyboardMarkup.setKeyboard(buttons);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }


    }

    private void history(Update update) {

    }

    private void category(Update update) {

    }

    private void search(Update update) {
    }

    private void startCommandReceived(Update update) {
        String answer = "Привет " + update.getMessage().getChat().getFirstName() + "!";
        sendMessage(update.getMessage().getChatId(), answer);
        //log.info("Replied to user" + update.getMessage().getChat().getFirstName());
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }

    }



//    private void registerUser(Update update) {
//        if (userRepository.findById(update.getMessage().getChatId()).isEmpty()) {
//            var chatId = update.getMessage().getChatId();
//            var chat = update.getMessage().getChat();
//
//            TelegramUser telegramUser = TelegramUser.builder()
//                    .chatId(chatId)
//                    .registeredAt(new Timestamp(System.currentTimeMillis()))
//                    .username(chat.getUserName())
//                    .firstName(chat.getFirstName())
//                    .lastName(chat.getLastName())
//                    .link("https://t.me/" + chat.getUserName())
//                    .build();
//            userRepository.save(telegramUser);
//        }
//    }
}