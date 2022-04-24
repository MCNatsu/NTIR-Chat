package me.aq.plugin.ntirchat.Event;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.aq.plugin.ntirchat.NTIRChat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Chat implements Listener{

    private NTIRChat plugin = NTIRChat.getPlugin();

    Map<UUID,Long> coolDowns = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){

        Player p = e.getPlayer();

        if(coolDowns.containsKey(p.getUniqueId())){
            if(System.currentTimeMillis() - coolDowns.get(p.getUniqueId())  < 2000){
                p.sendMessage(ChatColor.RED + "你每2秒只能發送一次訊息!");
                return;
            }
        }

        coolDowns.put(p.getUniqueId(),System.currentTimeMillis());

        String message = e.getMessage();

        //呼叫BungeeCordBridge
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(plugin.getServer().getMotd());
        out.writeUTF(p.getDisplayName());
        out.writeUTF(p.getUniqueId().toString());
        out.writeUTF(message);
        p.sendPluginMessage(plugin,"ntir:chat",out.toByteArray());


        e.setCancelled(true);
    }
}
