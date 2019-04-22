package com.github.alessio29.savagebot.cards;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.HashMap;
import java.util.Map;


/**
 * @author aless
 * <p>
 * This class contains utility methods for working with decks
 */

public class Decks {

    private static Map<Guild, Map<MessageChannel, Deck>> decks = new HashMap<>();

    public static Deck getDeck(Guild guild, MessageChannel channel) {

        Map<MessageChannel, Deck> guidlDecks = decks.get(guild);
        if (guidlDecks == null) {
            Decks.addDeck(guild, channel, Deck.createNewDeck());
        }
        return decks.get(guild).get(channel);
    }

    public static void addDeck(Guild guild, MessageChannel channel, Deck deck) {

        decks.computeIfAbsent(guild, k -> new HashMap<>())
                .put(channel, deck);
    }

}