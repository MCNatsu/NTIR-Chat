package me.aq.plugin.ntirchat.Event;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.aq.plugin.ntirchat.NTIRChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.ByteArrayOutputStream;

public class Chat implements Listener{

    private NTIRChat plugin = NTIRChat.getPlugin();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();

        String message = e.getMessage();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(plugin.getServer().getMotd());
        out.writeUTF(p.getName());
        out.writeUTF(p.getUniqueId().toString());
        out.writeUTF(message);
        p.sendPluginMessage(plugin,"ntir:chat",out.toByteArray());

    }
}
