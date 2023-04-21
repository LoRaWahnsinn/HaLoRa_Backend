package at.halora;

import at.halora.bot.TelegramBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {


        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        TelegramBot telegramBot = new TelegramBot();
        botsApi.registerBot(telegramBot);
    }

}