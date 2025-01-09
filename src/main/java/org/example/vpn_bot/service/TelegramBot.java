package org.example.vpn_bot.service;


import com.google.zxing.WriterException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.vpn_bot.config.BotConfig;
import org.example.vpn_bot.models.TelegramUser;
import org.example.vpn_bot.panel_x_ui.ApiOptions;
import org.example.vpn_bot.repositories.TelegramUserRepository;
import org.example.vpn_bot.service.Impl.SignUpServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

private final TelegramBotQRCode QR;
    private final TelegramUserRepository tgUserRepo;
    private final SignUpServiceImpl signUpService;
    private final ApiOptions apiOptions;

    @Value("${vpn.protocol}")
    private String protocol;
    @Value("${vpn.connect}")
    private String connect;
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
                    startCommand(update);
                    break;
                case "\uD83C\uDFE0 Главное меню \uD83C\uDFE0":
                    startCommand(update);
                    break;
                case "/info":
                    sendMessage(update.getMessage().getChatId(), "Разработчик бота @ezz_887\n ");
                    break;
                default:
                    sendMessage(update.getMessage().getChatId(), "Здравствуйте \uD83D\uDE4F\n" + "Ознакомьтесь с нашим Главным меню, тут вы найдете ответ на ваш вопрос \uD83E\uDEF6\uD83C\uDFFC");
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            Switcher(update);
        } else {
            log.warn("Сообщение не содержит текст.");
        }
    }


    private void startCommand(Update update) {
        Long id = getId(update);
        if (tgUserRepo.existsTelegramUserByChatId(id)) {
            menu(update);

        } else {
            signUpService.SignUp(update);
            String stickerId = "CAACAgIAAxkBAAENcTVneJYr6hPg8oKOf1Br_u2maNpiCQAChxoAAr1NOEq9sPjp-eU-CTYE";
            InputFile sticker = new InputFile(stickerId);
            SendSticker sendSticker = new SendSticker();
            sendSticker.setChatId(update.getMessage().getChatId());
            sendSticker.setSticker(sticker);
            try {
                execute(sendSticker);
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
        deleteLastBotMessage(update.getMessage().getChatId(), update.getMessage().getMessageId());

    }
    private String getConfig(Update update){
        Long id = getId(update);
        TelegramUser user = tgUserRepo.findByChatId(id).orElseThrow(() -> new IllegalArgumentException("Telegram user with chat ID " + id + " not found."));
        return protocol + user.getChatId() + connect + user.getUsername();
    }

    private void getTestVPN(Update update) {
        apiOptions.addClientToInbound(update);
        Long id = update.getCallbackQuery().getMessage().getChatId();
        TelegramUser user = tgUserRepo.findByChatId(id).orElseThrow(() -> new IllegalArgumentException("Telegram user with chat ID " + id + " not found."));
        int messId = update.getCallbackQuery().getMessage().getMessageId();
        String text = "✅ Доступ открыт ✅";
        EditMessageText message = new EditMessageText();
        message.setChatId(id);
        message.setText(text);
        message.setMessageId(messId);
        SendMessage messageVpn = new SendMessage();
        SendMessage messageVpn1 = new SendMessage();
        SendMessage messageVpn2 = new SendMessage();
        messageVpn.setChatId(id);
        messageVpn.setText("<b>Инструкция</b> \n\n" + "1. Скопируй ключ доступа.\n" + "(не открыть ссылку, а скопировать)\uD83D\uDC47\n\n");
        messageVpn.setParseMode("HTML");
        messageVpn1.setChatId(id);
        messageVpn1.setText(getConfig(update));
        messageVpn1.setParseMode("HTML");
        messageVpn2.setChatId(id);
        messageVpn2.setText("2. \uD83D\uDC47 Приложение можешь скачать отсюда: \uD83D\uDC47\n" + "<a href=\"https://play.google.com/store/apps/details?id=dev.hexasoftware.v2box\">Google Play</a>\n" + "<a href=\"https://apps.apple.com/ru/app/v2raytun/id6476628951\">App Store</a>\n\n" + "3. Выбери пункт <b>“Добавить из буфера и вставь ссылку”</b>.\n\n" + "<b>Готово!</b>\n\n" + "Приятного пользования 🙏");
        messageVpn2.setParseMode("HTML");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("\uD83C\uDFE0 Главное меню \uD83C\uDFE0");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        messageVpn2.setReplyMarkup(keyboardMarkup);

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
        try {
            execute(messageVpn1);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
        try {
            execute(messageVpn2);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }


    private void menu(Update update) {
        Long id = getId(update);
        SendMessage message = new SendMessage();
        message.setChatId(id);
        message.setParseMode("HTML");
        List<Long> day = getTime(id);
        if (!day.isEmpty()) {
            message.setText("<b>Аккаунт ID </b>" + id + "\n\n<b>Ключ истекает через\n</b>"
                    + day.get(0) + " дней " + day.get(1) + " часов " + day.get(2) + " минут ");

            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            row.add("\uD83C\uDFE0 Главное меню \uD83C\uDFE0");

            keyboardRows.add(row);
            keyboardMarkup.setKeyboard(keyboardRows);
            message.setReplyMarkup(keyboardMarkup);
            keyboardMarkup.setResizeKeyboard(true);
            message.setReplyMarkup(getMenuMarkup());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Error occurred: " + e.getMessage());
            }
        }
        message.setText("<b>Аккаунт ID </b>" + id + "\n\n<b>Ошибка</b>");

    }

    private void menuQuery(Update update) {
        Long id = getId(update);
        EditMessageText message = new EditMessageText();
        message.setChatId(id);
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        message.setParseMode("HTML");
        List<Long> day = getTime(id);
        if (!day.isEmpty()) {
            message.setText("<b>Аккаунт ID </b>" + id + "\n\n<b>Ключ истекает через\n</b>"
                    + day.get(0) + " дней " + day.get(1) + " часов " + day.get(2) + " минут ");

            message.setReplyMarkup(getMenuMarkup());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Error occurred: " + e.getMessage());
            }
        }
        message.setText("<b>Аккаунт ID </b>" + id + "\n\n<b>Ошибка</b>");

    }

    private void buy(Update update) {
        EditMessageText message = new EditMessageText();
        Long id = update.getCallbackQuery().getMessage().getChatId();
        int messId = update.getCallbackQuery().getMessage().getMessageId();
        message.setChatId(id);
        message.setMessageId(messId);
        message.setParseMode("HTML");
        List<Long> day = getTime(id);
        message.setText("У тебя осталось <b>" + day.get(0) + " дней " + "доступа </b>\n\n"
                + "<b>Продли дни доступа выгодно</b>\uD83D\uDC47");
        System.out.println(message.getText());
        message.setReplyMarkup(getBuyMarkup());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
    private void key(Update update)  {
        if (update.getCallbackQuery() == null || update.getCallbackQuery().getMessage() == null) {
            log.error("CallbackQuery or Message is null in the update");
            return;
        }

        Long id = update.getCallbackQuery().getMessage().getChatId();
        int messId = update.getCallbackQuery().getMessage().getMessageId();
        deleteLastBotMessage(id, messId);
        SendPhoto message = sendQRCode(id, getConfig(update));
        message.setChatId(id);
        message.setParseMode("HTML");


        List<Long> day = getTime(id);
        if (day == null || day.isEmpty()) {
            log.error("getTime(id) вернул null или пустой список.");
            message.setCaption("Не удалось получить информацию о сроке действия ключа.");
        } else {

            message.setCaption("Ключ доступа <b>\n\n" +
                    "Истекает через " + day.get(0) + " дней</b>.");
        }


        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: " + e.getMessage());
        }
        SendMessage message1 = new SendMessage();
        message1.setChatId(id);
        message1.setText(getConfig(update));
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> but = new ArrayList<>();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Назад");
        button.setCallbackData("MENU");
        but.add(button);
        buttons.add(but);
        keyboardMarkup.setKeyboard(buttons);
        message1.setReplyMarkup(keyboardMarkup);

        try {
            execute(message1);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message1: " + e.getMessage());
        }
    }
    private void help(Update update) {
        // Проверяем наличие callbackQuery и сообщения
        if (update.getCallbackQuery() == null || update.getCallbackQuery().getMessage() == null) {
            log.error("CallbackQuery or Message is null in the update");
            return;
        }

        // Получаем chatId и messageId
        Long id = update.getCallbackQuery().getMessage().getChatId();
        int messId = update.getCallbackQuery().getMessage().getMessageId();

        // Создаём сообщение с текстом помощи
        EditMessageText message = new EditMessageText();
        message.setChatId(id);
        message.setMessageId(messId);
        message.setParseMode("HTML");
        message.setText("Появились вопросы?\n\n" +
                "Напиши прямо в чат ✏\uFE0F");

        // Создаём клавиатуру
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Назад");
        button.setCallbackData("MENU");

        List<InlineKeyboardButton> but = new ArrayList<>();
        but.add(button);

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(but);

        keyboardMarkup.setKeyboard(buttons);
        message.setReplyMarkup(keyboardMarkup);

        // Отправляем сообщение
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending help message: " + e.getMessage());
        }
    }

    public void setup(Update update){

        if (update.getCallbackQuery() == null || update.getCallbackQuery().getMessage() == null) {
            log.error("CallbackQuery or Message is null in the update");
            return;
        }
        Long id = update.getCallbackQuery().getMessage().getChatId();
        int messId = update.getCallbackQuery().getMessage().getMessageId();

        EditMessageText message = new EditMessageText();
        message.setChatId(id);
        message.setMessageId(messId);
        message.setParseMode("HTML");
        message.setText("На каком устройстве нужно настроить VPN?");
        message.setReplyMarkup(getSetupMarkup());

        // Отправляем сообщение
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending help message: " + e.getMessage());
        }
    }


    private void startCommandReceived(Update update) {
        String answer = "Привет " + update.getMessage().getChat().getFirstName() + "!";
        sendMessage(update.getMessage().getChatId(), answer);
        log.info("Replied to user" + update.getMessage().getChat().getFirstName());
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

    private void deleteLastBotMessage(Long chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId.toString());
        deleteMessage.setMessageId(messageId);

        try {
            execute(deleteMessage);  // Удаляет сообщение
        } catch (TelegramApiException e) {
            log.error("Ошибка при удалении сообщения: " + e.getMessage());
        }
    }

    public Long getId(Update update) {
        Long id = null;  // Объявляем переменную id заранее, чтобы использовать ее в случае ошибки

        try {
            id = update.getCallbackQuery().getMessage().getChatId();
            if (id != null) {
                return id;
            }
        } catch (RuntimeException e) {
            log.error("Не удалось получить айди через getCallbackQuery: " + e.getMessage());
        }

        try {
            id = update.getMessage().getChatId();
            if (id != null) {
                return id;
            }
        } catch (RuntimeException e) {
            log.error("Не удалось получить айди через getMessage: " + e.getMessage());
        }

        return id;
    }

    public TelegramUser getUser(Update update) {
        Long id = update.getCallbackQuery().getMessage().getChatId();
        System.out.println("\n\n айди: " + id + "\n\n");
        if (id == null) {
            id = update.getMessage().getChatId();
        }


        Long finalId = id;
        TelegramUser userTg = tgUserRepo.findByChatId(finalId).orElseThrow(() -> new IllegalArgumentException("Telegram user with chat ID " + finalId + " not found."));
        return userTg;
    }

    public List<Long> getTime(Long id) {
        long lastTime = apiOptions.getTimeToLeft(id);
        long currentTime = Instant.now().toEpochMilli();
        long diff = lastTime - currentTime;
        if (diff > 0) {
            // Переводим разницу в секунды
            long seconds = diff / 1000;

            // Вычисляем дни, часы, минуты и секунды
            long days = seconds / (24 * 60 * 60);
            seconds %= (24 * 60 * 60); // Остаток секунд после дней

            long hours = seconds / (60 * 60);
            seconds %= (60 * 60); // Остаток секунд после часов

            long minutes = seconds / 60;
            seconds %= 60; // Остаток секунд после минут
            List<Long> arr = new ArrayList<>();
            arr.add(days);
            arr.add(hours);
            arr.add(minutes);
            arr.add(seconds);
            return arr;
        }
        return null;
    }
    public InlineKeyboardMarkup getSetupMarkup() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        // Добавление кнопки для iPhone/iPad
        InlineKeyboardButton buttonIPhone = new InlineKeyboardButton();
        buttonIPhone.setText("iPhone/iPad 🍏");
        buttonIPhone.setCallbackData("IPHONE");
        buttons.add(Collections.singletonList(buttonIPhone)); // Одна кнопка в строке

        // Добавление кнопки для Android
        InlineKeyboardButton buttonAndroid = new InlineKeyboardButton();
        buttonAndroid.setText("Android 🤖");
        buttonAndroid.setCallbackData("ANDROID");
        buttons.add(Collections.singletonList(buttonAndroid)); // Одна кнопка в строке

        // Добавление кнопки для Mac
        InlineKeyboardButton buttonMac = new InlineKeyboardButton();
        buttonMac.setText("Mac 🍎");
        buttonMac.setCallbackData("MAC");
        buttons.add(Collections.singletonList(buttonMac)); // Одна кнопка в строке

        // Добавление кнопки для Windows
        InlineKeyboardButton buttonWin = new InlineKeyboardButton();
        buttonWin.setText("Windows 🪟");
        buttonWin.setCallbackData("WIN");
        buttons.add(Collections.singletonList(buttonWin)); // Одна кнопка в строке

        // Добавление кнопки для телевизора
        InlineKeyboardButton buttonTv = new InlineKeyboardButton();
        buttonTv.setText("Телевизор 📺");
        buttonTv.setCallbackData("TV");
        buttons.add(Collections.singletonList(buttonTv)); // Одна кнопка в строке

        // Добавление кнопки "Назад"
        InlineKeyboardButton buttonBack = new InlineKeyboardButton();
        buttonBack.setText("Назад");
        buttonBack.setCallbackData("MENU");
        buttons.add(Collections.singletonList(buttonBack)); // Одна кнопка в строке

        // Установка кнопок в клавиатуру
        keyboardMarkup.setKeyboard(buttons);
        return keyboardMarkup;
    }


    public InlineKeyboardMarkup getMenuMarkup() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> but = new ArrayList<>();
        List<InlineKeyboardButton> but2 = new ArrayList<>();
        List<InlineKeyboardButton> but3 = new ArrayList<>();
        List<InlineKeyboardButton> but4 = new ArrayList<>();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        InlineKeyboardButton buttonBuy = new InlineKeyboardButton();
        buttonBuy.setText("\uD83D\uDCB8 Купить дни доступа");
        buttonBuy.setCallbackData("BUY");
        but.add(buttonBuy);

        InlineKeyboardButton buttonSetup = new InlineKeyboardButton();
        buttonSetup.setText("⚙\uFE0F Как настроить?");
        buttonSetup.setCallbackData("SETUP");
        but2.add(buttonSetup);
        InlineKeyboardButton buttonHelp = new InlineKeyboardButton();
        buttonHelp.setText("\uD83D\uDEA8 Помощь");
        buttonHelp.setCallbackData("HELP");
        but3.add(buttonHelp);
        InlineKeyboardButton buttonKey = new InlineKeyboardButton();
        buttonKey.setText("\uD83D\uDD11 Ключ");
        buttonKey.setCallbackData("KEY");
        but4.add(buttonKey);
        buttons.add(but);
        buttons.add(but4);
        buttons.add(but2);
        buttons.add(but3);

        keyboardMarkup.setKeyboard(buttons);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getBuyMarkup() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> but = new ArrayList<>();
        List<InlineKeyboardButton> but2 = new ArrayList<>();
        List<InlineKeyboardButton> but3 = new ArrayList<>();
        List<InlineKeyboardButton> but4 = new ArrayList<>();
        List<InlineKeyboardButton> but5 = new ArrayList<>();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        InlineKeyboardButton months = new InlineKeyboardButton();
        months.setText("199 руб на месяц");
        months.setCallbackData("MONTHS");
        but.add(months);

        InlineKeyboardButton threeMoths = new InlineKeyboardButton();
        threeMoths.setText("499 руб на 3 месяца");
        threeMoths.setCallbackData("THREEMONTHS");
        but2.add(threeMoths);

        InlineKeyboardButton sixMoths = new InlineKeyboardButton();
        sixMoths.setText("799 руб на полгода");
        sixMoths.setCallbackData("SIXMONTHS");
        but3.add(sixMoths);

        InlineKeyboardButton year = new InlineKeyboardButton();
        year.setText("1499 руб на год");
        year.setCallbackData("YEAR");
        but4.add(year);

        InlineKeyboardButton home = new InlineKeyboardButton();
        home.setText("Выйти в главное меню");
        home.setCallbackData("MENU");
        but5.add(home);

        buttons.add(but);
        buttons.add(but2);
        buttons.add(but3);
        buttons.add(but4);
        buttons.add(but5);

        keyboardMarkup.setKeyboard(buttons);
        return keyboardMarkup;
    }

    public SendPhoto sendQRCode(Long chatId, String qrText){

            // Генерация QR-кода
        ByteArrayInputStream qrCodeStream = null;
        try {
            qrCodeStream = QR.generateQRCodeStream(qrText, 300, 300);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Подготовка сообщения с изображением
            InputFile inputFile = new InputFile(qrCodeStream, "qr-code.png");
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(inputFile);
            //sendPhoto.setCaption("Ваш QR-код:"); // Опционально, можно добавить подпись

            return sendPhoto;
    }

    private void Check(Update update, String period){
        if (update.getCallbackQuery() == null || update.getCallbackQuery().getMessage() == null) {
            log.error("CallbackQuery or Message is null in the update");
            return;
        }

        Long id = update.getCallbackQuery().getMessage().getChatId();
        int messId = update.getCallbackQuery().getMessage().getMessageId();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        EditMessageText message = new EditMessageText();
        message.setChatId(id);
        message.setMessageId(messId);
        message.setParseMode("HTML");
        message.setText("Нужен ли вам чек?");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> but = new ArrayList<>();
        List<InlineKeyboardButton> but2 = new ArrayList<>();
        InlineKeyboardButton months = new InlineKeyboardButton();
        months.setText("Да, нужен");
        months.setCallbackData("TRUE");
        but.add(months);
        InlineKeyboardButton threeMoths = new InlineKeyboardButton();
        threeMoths.setText("Нет");
        threeMoths.setCallbackData("FALSE");
        but.add(threeMoths);
        InlineKeyboardButton home = new InlineKeyboardButton();
        home.setText("Выйти в главное меню");
        home.setCallbackData("MENU");
        but2.add(home);
        keyboardMarkup.setKeyboard(buttons);
        message.setReplyMarkup(keyboardMarkup);

    }
    private String Payments(Update update, String period){
        return null;
    }

    public void Switcher(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        boolean check;
        switch (callbackData) {
            case "ACCEPT":
                getTestVPN(update);
                break;
            case "BUY":
                buy(update);
                break;
            case "SETUP":
                setup(update);
                break;
            case "HELP":
                help(update);
                break;
            case "KEY":
                key(update);
                break;
            case "MOTHS":
                Payments(update, callbackData);
                break;
            case "THREEMOTHS":
                Check(update, callbackData);
                break;
            case "SIXMOTHS":
                Check(update, callbackData);
                break;
            case "YEAR":
                Check(update, callbackData);
                break;
            case "IPHONE":
                break;
            case "ANDROID":
                break;
            case "MAC":
                break;
            case "WIN":
                break;
            case "TV":
                break;
            case "MENU":
                menuQuery(update);
                break;

            default:
                sendMessage(update.getCallbackQuery().getMessage().getChatId(), "Здравствуйте \uD83D\uDE4F\n" + "Ознакомьтесь с нашим Главным меню, тут вы найдете ответ на ваш вопрос \uD83E\uDEF6\uD83C\uDFFC");
                break;
        }
    }
}