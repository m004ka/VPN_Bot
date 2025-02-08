package org.example.vpn_bot.service.keyboard;

import lombok.RequiredArgsConstructor;
import org.example.vpn_bot.Enum.Period;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyBoardService {
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



    // Метод для создания клавиатуры
    public InlineKeyboardMarkup getMenuMarkup() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        // Первая кнопка - Купить дни доступа
        InlineKeyboardButton buttonBuy = new InlineKeyboardButton();
        buttonBuy.setText("\uD83D\uDCB8 Купить дни доступа");
        buttonBuy.setCallbackData("BUY");

        // Вторая кнопка - Как настроить
        InlineKeyboardButton buttonSetup = new InlineKeyboardButton();
        buttonSetup.setText("⚙\uFE0F Как настроить?");
        buttonSetup.setCallbackData("SETUP");

        // Третья кнопка - Обновить (MENU)
        InlineKeyboardButton buttonUpdate = new InlineKeyboardButton();
        buttonUpdate.setText("\uD83D\uDD04 Обновить");
        buttonUpdate.setCallbackData("MENU_UPDATE");

        // Четвёртая кнопка - Помощь
        InlineKeyboardButton buttonHelp = new InlineKeyboardButton();
        buttonHelp.setText("\uD83D\uDEA8 Помощь");
        buttonHelp.setUrl("https://t.me/Svetlyachok_support");

        // Пятая кнопка - Ключ
        InlineKeyboardButton buttonKey = new InlineKeyboardButton();
        buttonKey.setText("\uD83D\uDD11 Ключ");
        buttonKey.setCallbackData("KEY");

        InlineKeyboardButton buttonDownload = new InlineKeyboardButton();
        buttonDownload.setText("\uD83D\uDD04 Скачать VPN");
        buttonDownload.setCallbackData("DOWNLOAD");

        // Добавляем кнопки в ряды
        buttons.add(List.of(buttonBuy)); // 1 ряд
        buttons.add(List.of(buttonKey)); // 4 ряд
        buttons.add(List.of(buttonSetup, buttonDownload)); // 2 ряд
        buttons.add(List.of(buttonUpdate, buttonHelp)); // 3 ряд

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

    public InlineKeyboardMarkup backKeyBoard(){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> but = new ArrayList<>();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Назад");
        button.setCallbackData("MENU");
        but.add(button);
        buttons.add(but);
        keyboardMarkup.setKeyboard(buttons);

        return keyboardMarkup;
    }

    public InlineKeyboardMarkup backKeyBoardDeleteMess(){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> but = new ArrayList<>();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Назад");
        button.setCallbackData("MENU_delete");
        but.add(button);
        buttons.add(but);
        keyboardMarkup.setKeyboard(buttons);

        return keyboardMarkup;
    }

    public InlineKeyboardMarkup checkKeyBoard(Period period){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> but = new ArrayList<>();
        List<InlineKeyboardButton> but2 = new ArrayList<>();
        InlineKeyboardButton months = new InlineKeyboardButton();
        months.setText("Да, нужен");
        months.setCallbackData("TRUE_" + period.getMonths());
        but.add(months);
        InlineKeyboardButton threeMoths = new InlineKeyboardButton();
        threeMoths.setText("Нет");
        threeMoths.setCallbackData("FALSE");
        but.add(threeMoths);
        InlineKeyboardButton home = new InlineKeyboardButton();
        home.setText("Назад");
        home.setCallbackData("BUY");
        but2.add(home);
        buttons.add(but);
        buttons.add(but2);
        keyboardMarkup.setKeyboard(buttons);

        return keyboardMarkup;
    }


     public InlineKeyboardMarkup getAccess(){
         InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
         List<InlineKeyboardButton> but = new ArrayList<>();
         List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
         InlineKeyboardButton button = new InlineKeyboardButton();
         button.setText("✅ Получить доступ ✅");
         button.setCallbackData("ACCEPT");
         but.add(button);
         buttons.add(but);
         keyboardMarkup.setKeyboard(buttons);
         return keyboardMarkup;
     }

    public InlineKeyboardMarkup payKeyBoard(Period period){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> but = new ArrayList<>();
        List<InlineKeyboardButton> but2 = new ArrayList<>();
        InlineKeyboardButton months = new InlineKeyboardButton();
        months.setText("Личный Кабинет");
        months.setCallbackData("MENU");
        but.add(months);
        InlineKeyboardButton threeMoths = new InlineKeyboardButton();
        threeMoths.setText("Отменить оплату");
        threeMoths.setCallbackData("FALSE");
        but.add(threeMoths);
        InlineKeyboardButton home = new InlineKeyboardButton();
        home.setText("Назад");
        home.setCallbackData("BUY");
        but2.add(home);
        buttons.add(but);
        buttons.add(but2);
        keyboardMarkup.setKeyboard(buttons);

        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getDownloadKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        // Первый ряд - iPhone и Android
        InlineKeyboardButton buttonIphone = new InlineKeyboardButton();
        buttonIphone.setText("📱 iPhone");
        buttonIphone.setUrl("https://apps.apple.com/ru/app/streisand/id6450534064"); // Ссылка на iPhone

        InlineKeyboardButton buttonAndroid = new InlineKeyboardButton();
        buttonAndroid.setText("🤖 Android");
        buttonAndroid.setUrl("https://play.google.com/store/apps/details?id=com.v2ray.ang&pli=1"); // Ссылка на Android

        // Второй ряд - Windows и MacOS
        InlineKeyboardButton buttonWindows = new InlineKeyboardButton();
        buttonWindows.setText("💻 Windows");
        buttonWindows.setUrl("https://github.com/hiddify/hiddify-next/releases/download/v2.5.7/Hiddify-Windows-Setup-x64.exe"); // Ссылка на Windows

        InlineKeyboardButton buttonMac = new InlineKeyboardButton();
        buttonMac.setText("🍏 MacOS");
        buttonMac.setUrl("https://apps.apple.com/us/app/streisand/id6450534064?l=ru"); // Ссылка на MacOS

        // Третий ряд - Huawei
        InlineKeyboardButton buttonHuawei = new InlineKeyboardButton();
        buttonHuawei.setText("☎ Huawei");
        buttonHuawei.setUrl("https://github.com/2dust/v2rayNG/releases/download/1.8.5/v2rayNG_1.8.5.apk"); // Ссылка на Huawei

        // Четвёртый ряд - Личный кабинет
        InlineKeyboardButton buttonProfile = new InlineKeyboardButton();
        buttonProfile.setText("👤 Личный кабинет");
        buttonProfile.setCallbackData("MENU"); // Обработка в боте

        // Добавляем кнопки в ряды
        buttons.add(List.of(buttonIphone, buttonAndroid)); // 1 ряд
        buttons.add(List.of(buttonWindows, buttonMac));    // 2 ряд
        buttons.add(List.of(buttonHuawei));               // 3 ряд
        buttons.add(List.of(buttonProfile));              // 4 ряд

        keyboardMarkup.setKeyboard(buttons);
        return keyboardMarkup;
    }


}
