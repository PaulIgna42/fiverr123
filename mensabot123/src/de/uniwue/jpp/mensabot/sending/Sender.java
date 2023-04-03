package de.uniwue.jpp.mensabot.sending;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Optional;

public interface Sender {

    Optional<String> send(String msg);

    static Sender createDummySender() {
        return new Sender() {
            @Override
            public Optional<String> send(String msg) {
                System.out.println(msg);
                return Optional.empty();
            }
        };
    }

    /* Falls Sie Aufgabenteil 2 nicht bearbeiten, kann diese Methode ignoriert werden */
    static Sender createTelegramSender(HttpClient client, String apiToken, List<String> chatIds) {
        throw new UnsupportedOperationException();
    }
}
