package org.example.vpn_bot.service.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public InlineKeyboardMarkup checkKeyBoard(){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
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

}
