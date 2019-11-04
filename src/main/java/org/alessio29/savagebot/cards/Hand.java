package org.alessio29.savagebot.cards;

import java.util.ArrayList;
import java.util.List;

public class Hand {

	private String user;
	private List<Card> cards = new ArrayList<Card>();
	
	public Hand(String userId) {
		this.user = userId;
	}

	public String getUserId() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public List<Card> getCards() {
		return cards;
	}
	
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public void clear() {
		this.cards.clear();
	}
}
