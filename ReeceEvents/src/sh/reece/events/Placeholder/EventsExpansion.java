package sh.reece.events.Placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import sh.reece.events.Event;
import sh.reece.events.EventsPlugin;

import org.bukkit.entity.Player;

public class EventsExpansion extends PlaceholderExpansion {

    private EventsPlugin eventsPlugin;
    private Event eventClass = Event.getInstance();

    public EventsExpansion(EventsPlugin pl){
        this.eventsPlugin = pl;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "events";
    }

    @Override
    public String getAuthor() {
        return "Reecepbcups";
    }

    @Override
    public String getVersion() {
        return eventsPlugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        // %eventplugin_playercount%
        if(identifier.equals("playercount")){
            return String.valueOf(eventClass.getPlayerCount());
        }
        if(identifier.equals("eventstarted")){
            return (eventClass.isStarted() ? "Yes" : "No");
        }
        if(identifier.equals("inevent")){
            if(player == null){
                return null; // "?"
            }
            return (eventClass.isInEvent(player) ? "Yes" : "No");
        }
        if(identifier.equals("startcount")){
            return eventClass.getStartCount()+"";
        }
        return null;
    }
}
