package com.github.alessio29.savagebot.cards;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;


public class Hands {

    private static Map<Guild, Map<User, Hand>> hands = new HashMap<>();

	public static Hand getHand(Guild guild, User user) {
        return hands
                .computeIfAbsent(guild, k -> new HashMap<>())
                .computeIfAbsent(user, Hand::new);
	}
}