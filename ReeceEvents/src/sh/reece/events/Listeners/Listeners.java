package sh.reece.events.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

import sh.reece.events.Event;
import sh.reece.events.EventsPlugin;
import sh.reece.events.Util;
import sh.reece.events.scoreboardmanager.Scoreboard;

public class Listeners implements Listener {
    private Event eventClass = Event.getInstance();
    private EventsPlugin main = EventsPlugin.getInstance();
    private String prefix = main.getPrefix();
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
    	
    	
    	main.getScoreboardManager().createBoard(e.getPlayer());
    	
    	
        if(!eventClass.isStarted()) return;        
        if(!main.getConfig().getBoolean("addPlayersAfterStart")) return;       
        
        eventClass.addPlayer(e.getPlayer());
        Util.coloredBroadcast(prefix + "Player Added: &6" + e.getPlayer().getName());
    }

    
    @EventHandler
    public void playerAttack(EntityDamageByEntityEvent e){
        if(!eventClass.isStarted()) return;
        
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
        	
        	if(e.getDamager().hasPermission("event.pvpbypass")) { return; }
        	
        	if(!eventClass.isPvPAllowed()) {
        		e.setCancelled(true);
        		Util.coloredMessage(e.getDamager(), "&cPVP is off, you can not attack at this time");
        	}
        }
        
    }
    
    @EventHandler
    public void playerPlace(BlockPlaceEvent e){
        if(!eventClass.isStarted()) return;
     
        if(!eventClass.isBuildAllowed()) {
        	if(e.getPlayer().hasPermission("event.buildbypass")) { return; }

        	e.setCancelled(true);
        	Util.coloredMessage(e.getPlayer(), "&cBuilding is off, you can not build at this time");
        }
    }
    @EventHandler
    public void playerBreak(BlockBreakEvent e){
        if(!eventClass.isStarted()) return;
     
        if(!eventClass.isBreakAllowed()) {
        	if(e.getPlayer().hasPermission("event.breakbypass")) {
        		return;
        	}

        	e.setCancelled(true);
        	Util.coloredMessage(e.getPlayer(), "&cBreaking is off, you can not break at this time");
        }
    }
    
    
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        if(!eventClass.isStarted()) return;
        eventClass.removePlayer(e.getPlayer());
    }
    @EventHandler
    public void onLeave(PlayerKickEvent e){
        if(!eventClass.isStarted()) return;
        eventClass.removePlayer(e.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if(!eventClass.isStarted()) return;
        eventClass.removePlayer(e.getEntity());
        e.getEntity().sendMessage(main.getPrefix() + "You have been eliminated!");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(final PlayerRespawnEvent e){
        e.setRespawnLocation(main.getJailLocation());
        
        Bukkit.getScheduler().runTaskLater(main, new Runnable() {
            public void run() {
                e.getPlayer().teleport(main.getJailLocation());
            }
        }, 5);
    }
    
    
    
    
    
    
    
}
