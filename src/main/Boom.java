package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Boom extends GameObject{
	private Integer gameType;
	private Integer currentNum = 1;
	private Integer currentPlayer = 0;

	public Boom(int gameType) {
		this.gameType = gameType;
		users = new ArrayList<User>();
		chat = new ArrayList<>();
	}

	public String main(String input) {
		input = input.toLowerCase().trim();
		if (!input.startsWith(currentPlayer + ":"))
			return "wrong player";
		int index = currentPlayer.toString().length();
		boolean isBoom = currentNum % gameType == 0 || currentNum.toString().contains(gameType.toString());
		input = input.substring(index + 1);
		try {
			int numberGiven = Integer.parseInt(input);
			if (isBoom) {
				removeUser(currentPlayer);
				return "you snooze you loze";
			}
			else if(numberGiven != currentNum) {
				removeUser(currentPlayer);
				return "you snooze you loze";
			}
			advance();
		} catch (Exception e) {
			if (!input.equals("boom"))
				return "defaq does " + input + " mean?";
			if (isBoom)
				advance();
			else
				removeUser(currentPlayer);
			return isBoom ? "gg" : "you snooze you loze";
		}
		return null;
	}

	private void advance() {
		currentPlayer = (currentPlayer + 1) % users.size();
		currentNum++;
	}
}
