package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MyTelegramBot extends TelegramLongPollingBot {

    private final String botToken = "7966593805:AAE4str0CcoaRoqYio2GCu3aq3Hiqfl0MFU";
    private final String botUsername = "Probeus_bot";

    private static final String FILE_PATH = "messages.txt";

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String receivedText = update.getMessage().getText();


            writeMessageToFile(receivedText);


            String recentMessages = readMessagesFromFile();


            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Ты написал: " + receivedText + "\nПоследние сообщения:\n" + recentMessages);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    private void writeMessageToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для чтения последних сообщений из файла (макс. 5)
    private String readMessagesFromFile() {
        StringBuilder messages = new StringBuilder();
        try {
            List<String> allMessages = Files.readAllLines(Paths.get(FILE_PATH));
            int start = Math.max(0, allMessages.size() - 5);
            for (int i = start; i < allMessages.size(); i++) {
                messages.append(allMessages.get(i)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages.toString();
    }
}
