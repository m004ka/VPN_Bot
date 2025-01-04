package org.example.vpn_bot.service;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.vpn_bot.config.BotConfig;
import org.example.vpn_bot.repositories.TelegramUserRepository;
import org.example.vpn_bot.service.Impl.SignUpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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

    private final TelegramUserRepository tgUserRepo;
    private final SignUpServiceImpl signUpService;


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
                case "/menu":
                    menu(update);
                    break;
                case "/start":
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
                    sendMessage(update.getMessage().getChatId(), "Привет");
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            switch (callbackData) {
                case "ACCEPT":
                    getTestVPN(update);
                    break; // Добавляем break, чтобы предотвратить попадание в default
                default:
                    sendMessage(update.getCallbackQuery().getMessage().getChatId(), "Привет");
                    break; // Добавляем break здесь тоже
            }
        } else {
            log.warn("Сообщение не содержит текст.");
        }
    }


    private void startCommand(Update update) {

        if (tgUserRepo.existsTelegramUserByChatId(update.getMessage().getChatId())) {
            menu(update);
        } else {
            signUpService.SignUp(update);
            String stickerId = "CAACAgIAAxkBAAENcTVneJYr6hPg8oKOf1Br_u2maNpiCQAChxoAAr1NOEq9sPjp-eU-CTYE";
            InputFile sticker = new InputFile(stickerId);
            SendSticker sendSticker = new SendSticker();
            sendSticker.setChatId(update.getMessage().getChatId());
            sendSticker.setSticker(sticker);
            try {
                execute(sendSticker); // Отправляем стикер
            } catch (TelegramApiException e) {
                e.printStackTrace();
                log.error("Хасбик не доставлен: " + e.getMessage());
            }

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
                log.error("Сообщение не доставлено: " + e.getMessage());
            }
        }

    }

    private void getTestVPN(Update update){
        Long id = update.getCallbackQuery().getMessage().getChatId();
        int messId = update.getCallbackQuery().getMessage().getMessageId();
        String text = "✅ Доступ открыт ✅";
        EditMessageText message = new EditMessageText();
        message.setChatId(id);
        message.setText(text);
        message.setMessageId(messId);
        SendMessage messageVpn = new SendMessage();
        messageVpn.setChatId(id);
        messageVpn.setText("Вот твой конфиг");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
        try {
            execute(messageVpn);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }


    private void menu(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setParseMode("HTML");
        message.setText("<b>Вы находитесь в главном меню</b> ");


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