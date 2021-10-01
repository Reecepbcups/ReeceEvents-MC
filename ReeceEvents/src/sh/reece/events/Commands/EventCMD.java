package sh.reece.events.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import sh.reece.events.Event;
import sh.reece.events.EventsPlugin;
import sh.reece.events.Util;

public class EventCMD implements CommandExecutor, TabCompleter {

    private EventsPlugin main = EventsPlugin.getInstance();
    private Event eventClass = Event.getInstance();
    private Random random;
    String prefix = main.getPrefix();
    String color1 = main.getColorOne();
    String color2 = main.getColorTwo();
    
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
    	random = new Random();
    	
        if(!sender.hasPermission(main.getConfig().getString("permission"))){
            sender.sendMessage(prefix + ChatColor.RED + "No permission!");
            return false;
        }

        if(args.length == 0){
        	helpMessage(sender);
            return false;
        }

        
        switch (args[0]) {
        
        	case "pvp":        		
        		if(!eventClass.isStarted()) {
        			sender.sendMessage(prefix + ChatColor.RED + "The event hasn't started!");
            		return false;
            	}     
        		
        		if(args.length == 2) {           			
        			switch (args[1]) {
					case "on":
					case "enable":
						eventClass.setPvP(true);
						sender.sendMessage(prefix + ChatColor.GREEN + "Event PVP Enabled");
						break;

					case "off":
					case "disable":
						eventClass.setPvP(false);
						sender.sendMessage(prefix + ChatColor.RED + "Event PVP Disabled");
						break;
						
					default:
						sender.sendMessage(prefix + ChatColor.RED + "/event pvp <on/off>");
						break;
					}        			        			
        		} else {
        			Util.coloredMessage(sender, prefix+"&c/event pvp <on/off> &7- &eCurrently: " + eventClass.isPvPAllowed());
        		}        		
        		return false;
        		
        	case "build":    
        	case "building": 
        		if(!eventClass.isStarted()) {
        			sender.sendMessage(prefix + ChatColor.RED + "The event hasn't started!");
            		return false;
            	}     
        		
        		if(args.length != 2) { 
        			Util.coloredMessage(sender, prefix+"&c/event build <on/off> &7- &eCurrently: "+eventClass.isBuildAllowed());
        			return false;
        		}
			
        		switch (args[1]) {
        		case "on":
        		case "enable":
        			eventClass.setBuild(true);
        			sender.sendMessage(prefix + ChatColor.GREEN + "Event Building Enabled");
        			break;

        		case "off":
        		case "disable":
        			eventClass.setPvP(false);
        			sender.sendMessage(prefix + ChatColor.RED + "Event Building Disabled");
        			break;

        		default:
        			sender.sendMessage(prefix + ChatColor.RED + "/event build <on/off>");
        			break;
        		}        
        		return false;
        		
        	case "break":   
        	case "breaking":
        		if(!eventClass.isStarted()) {
        			sender.sendMessage(prefix + ChatColor.RED + "The event hasn't started!");
            		return false;
            	}     
        		
        		if(args.length != 2) { 
        			Util.coloredMessage(sender, prefix+"&c/event break <on/off> &7- &eCurrently: "+eventClass.isBuildAllowed());
        			return false;
        		}
			
        		switch (args[1]) {
        		case "on":
        		case "enable":
        			eventClass.setBreak(true);
        			sender.sendMessage(prefix + ChatColor.GREEN + "Event Block Breaking Enabled");
        			break;

        		case "off":
        		case "disable":
        			eventClass.setBreak(false);
        			sender.sendMessage(prefix + ChatColor.RED + "Event Block Breaking Disabled");
        			break;

        		default:
        			sender.sendMessage(prefix + ChatColor.RED + "/event break <on/off>");
        			break;
        		}        
        		return false;
        		
            case "start":            	
            	if(eventClass.isStarted()) {
            		sender.sendMessage("An event is already running, stop it before you can start.");
            		return false;
            	}
            	
                eventClass.startEvent();
                Bukkit.broadcastMessage(prefix + "The event has begun!");
                sender.sendMessage(prefix + "Adding everyone in the server.");
                return false;
                
            case "stop":
            	if(eventClass.isStarted()) {
            		sender.sendMessage("Stopping event...");
            		eventClass.endEvent();
            	} else {
            		sender.sendMessage("Event is not running");
            	}
                
            case "count":
            case "alive":
                if(!eventClass.isStarted()){
                    sender.sendMessage(prefix + ChatColor.RED + "The event hasn't started!");
                    return false;
                }
                sender.sendMessage(prefix + "Event count: " + color2 + eventClass.getPlayerCount());
                
                String playersNames = "";
                for(Player p : eventClass.getPlayers()) {
                	playersNames += p.getName()+", ";
                }                
                sender.sendMessage(color1+"Alive: "+color2 + playersNames);
                
                return false;
            case "revive":
                if(!eventClass.isStarted()){
                	Util.coloredMessage(sender, "&cThe event hasn't started!");
                    return false;
                }
                if(args.length < 2){
                	Util.coloredMessage(sender, "&cPlease specify a player!");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if(player == null){
                	Util.coloredMessage(sender, "&cPlayer not found!");
                    return false;
                }
                if(eventClass.isInEvent(player)){
                    Util.coloredMessage(sender, "&cThat player is still in the event!");
                    return false;
                }
                eventClass.addPlayer(player);
                player.teleport((Player) sender);
                sender.sendMessage(prefix + color2 + player.getName() + color1 + " has been revived.");
                Util.coloredMessage(player, prefix + color1 + "You have been revived!");
                return false;
                
            case "isalive":
                if(!eventClass.isStarted()){
                	Util.coloredMessage(sender, "&cThe event hasn't started!");
                    return false;
                }
                if(args.length < 2){
                	Util.coloredMessage(sender, "&cPlease specify a player!");
                    return false;
                }
                Player p = Bukkit.getPlayer(args[1]);
                if(p == null){
                	Util.coloredMessage(sender, "&cPlayer not found!");
                    return false;
                }
                sender.sendMessage(prefix + color2 + p.getName() + color1 + " is " + (eventClass.isInEvent(p) ? ChatColor.GREEN + "ALIVE" : ChatColor.RED + "DEAD"));
                return false;
                
            case "remove":
                if(!eventClass.isStarted()){
                	Util.coloredMessage(sender, "&cThe event hasn't started!");
                    return false;
                }
                if(args.length < 2){
                	Util.coloredMessage(sender, "&cPlease specify a player!");
                    return false;
                }
                Player pl = Bukkit.getPlayer(args[1]);
                if(pl == null){
                	Util.coloredMessage(sender, "&cPlayer not found!");
                    return false;
                }
                if(!eventClass.isInEvent(pl)){
                	Util.coloredMessage(sender, "&cThat player is not in the event!");
                }
                eventClass.removePlayer(pl);
                sender.sendMessage(prefix + color2 + pl.getName() + color1 + " has been excluded from the event!");
                pl.sendMessage(prefix + color1 + "You've been removed from the event.");
                return false;
                
            case "setjail":
                if(!(sender instanceof Player)){
                	Util.coloredMessage(sender, "&cSorry! This is a player-only command!");
                    return false;
                }
                
                Location loc = ((Player) sender).getLocation();
                main.getConfig().set("jail", Util.locationToStringWithPitch(loc));
                
                main.saveConfig();
                main.reloadJailLoacation();
                Util.coloredMessage(sender, prefix + 
                		"Successfully updated jail location to: (&f" + loc.getBlockX() + " , " + loc.getBlockZ() + "&6)");
                return false;
                
            case "tpalive":
                if(!(sender instanceof Player)) {
                	Util.coloredMessage(sender, prefix + "Sorry, you can't do that!");
                    return false;
                }
                if(!eventClass.isStarted()){
                	Util.coloredMessage(sender, "&cThe event hasn't started!");
                    return false;
                }
                Util.coloredMessage(sender, prefix + "Teleporting all alive players (3 seconds).");
                Location l = ((Player) sender).getLocation();
                for(Player t : eventClass.getPlayers()){
                	new BukkitRunnable(){
        				public void run() {
        					t.teleport(l);
        				}
        			}.runTaskLater(main, random.nextInt(60)); // 3 second delay (3*20)
                }
                return false;
                
            case "tpdead":
                if(!eventClass.isStarted()){
                	Util.coloredMessage(sender, "&cThe event hasn't started!");
                    return false;
                }
                if(!(sender instanceof Player)) {
                	Util.coloredMessage(sender, prefix + "Sorry, you can't do that!");
                    return false;
                }
                Util.coloredMessage(sender, prefix + "Teleporting all dead players (3 seconds).");
                Location location = ((Player) sender).getLocation();
                for(Player t : main.getServer().getOnlinePlayers()){                	
                	new BukkitRunnable(){
                		public void run() {
                			if(!eventClass.getPlayers().contains(t)) { 
                				t.teleport(location); 
                			}
                		}
                	}.runTaskLater(main, random.nextInt(60)); // 3 second delay (3*20)
                }
                return false;
                
            default:
            	helpMessage(sender);
                return false;
        }
    }
    
    public void helpMessage(CommandSender sender) {
    	sender.sendMessage(ChatColor.translateAlternateColorCodes('&',color1 + ChatColor.BOLD + "Events " + ChatColor.GRAY + "(1/1)\n" +
                color1 + "/event start &8- " + color2 + "Start the event!\n" +
                color1 + "/event count &8- " + color2 + "List of players alive!\n" +
                color1 + "/event revive <player> &8- " + color2 + "Put player back into event.\n" +
                color1 + "/event isalive <player> &8- " + color2 + "Checks player alive status.\n" +
                color1 + "/event remove <player> &8- " + color2 + "Remove a player from the event.\n" +
                color1 + "/event setjail &8- " + color2 + "Sets the location for the event respawn.\n\n" +
                
                color1 + "/event pvp <on/off>&8- " + color2 + "Toggles pvp on and off for all.\n" +
                color1 + "/event build <on/off>&8- " + color2 + "Toggles buidling blocks for all.\n" + 
                color1 + "/event break <on/off>&8- " + color2 + "Toggles Breaking blocks for all." 
        ));
    }
    
    
    private List<String> possibleArugments = new ArrayList<String>();
	private List<String> result = new ArrayList<String>();
	private String[] toggles = {"on", "off"};
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {		
		if(possibleArugments.isEmpty()) {
			possibleArugments.add("start");
			possibleArugments.add("stop");			
			possibleArugments.add("count"); 
			possibleArugments.add("revive");
			possibleArugments.add("isalive"); 
			possibleArugments.add("exclude"); 
			possibleArugments.add("tpalive");
			possibleArugments.add("tpdead");
			possibleArugments.add("setjail");
			possibleArugments.add("pvp");
			possibleArugments.add("build");
			possibleArugments.add("break");
		}		
		result.clear();
		if(args.length == 1) {
			
			for(String a : possibleArugments) {
				if(a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);			
				}
			}
			return result;
		}	
		if(args[0].equalsIgnoreCase("pvp") || 
				args[0].equalsIgnoreCase("build") || 
				args[0].equalsIgnoreCase("break") && args.length == 2) {
			
			for(String a : toggles) {
				if(a.toLowerCase().startsWith(args[1].toLowerCase())) {
					result.add(a);			
				}
			}
			return result;
		}		
		return null;
	}
    
    
    
}
