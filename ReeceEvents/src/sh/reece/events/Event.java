package sh.reece.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Event {

	private EventsPlugin main = EventsPlugin.getInstance();
	private static Event instance;
	private boolean started = false;
	private ArrayList<Player> players = new ArrayList<Player>();
	private int startCount;
	
	private Boolean canPVP = true;
	private Boolean canBUILD = true;
	private Boolean canBREAK = true;
	
	public boolean isPvPAllowed() {
		return canPVP;
	}
	public void setPvP(Boolean value) {
		if(value != canPVP) {
			canPVP = value;
		}		
	}
	
	public boolean isBuildAllowed() {
		return canBUILD;
	}
	public void setBuild(Boolean value) {
		if(value != canBUILD) {
			canBUILD = value;
		}		
	}
	
	public boolean isBreakAllowed() {
		return canBREAK;
	}
	public void setBreak(Boolean value) {
		if(value != canBREAK) {
			canBREAK = value;
		}		
	}
	
	
	public boolean isInEvent(Player p) {
		if (!isStarted())
			return false;
		return players.contains(p);
	}

	public void addPlayer(Player p) {
		if (!isStarted())
			return;
		players.add(p);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void removePlayer(Player p) {
		if (!isStarted())
			return;
		players.remove(p);
		if (players.size() <= 1) {
			if (players.size() < 1) {
				winMessage();
			} else {
				winMessage(players.get(0));
				endEvent();
			}
		}
	}

	public void clearList() {
		players.clear();
	}

	public int getPlayerCount() {
		if (!isStarted())
			return 0;
		return players.size();
	}

	public boolean isStarted() {
		return started;
	}

	public int getStartCount() {
		return startCount;
	}

	public void startEvent() {
		this.started = true;
		for (Player p : Bukkit.getOnlinePlayers()) {
			addPlayer(p);
		}
		this.startCount = players.size();
	}

	public void endEvent() {
		this.started = false;
		clearList();
		startCount = 0;
	}

	public static Event getInstance() {
		if (instance == null) {
			instance = new Event();
		}
		return instance;
	}

	private void winMessage(Player winner) {
		Util.coloredBroadcast("&6             EVENT ENDED             ");
		Util.coloredBroadcast("&6Winner: &f" + winner.getName());
		Util.coloredBroadcast("&6Congratulations &f" + winner.getName() + "&6 and thank you for playing everyone!");
	}

	private void winMessage() {
		Util.coloredBroadcast("&6             EVENT ENDED             ");
		Util.coloredBroadcast("&6No one won! :(");
		Util.coloredBroadcast("&6Thank you for playing everyone!");
	}
}
