package me.aq.plugin.ntirchat.Event;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.aq.plugin.ntirchat.NTIRChat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class onChatCall implements PluginMessageListener {

    private NTIRChat plugin = NTIRChat.getPlugin();

    @Override
    public void onPluginMessageReceived(@NotNull String channel, Player player, @NotNull byte[] message) {

        if(!channel.equals("ntir:chat")){
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String motd = in.readUTF();
        String name = in.readUTF();
        String uuid = in.readUTF();
        String rawMsg = in.readUTF();

        OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
        //檢查是否包含網址
        List<String> urls = getUrls(rawMsg);

        //預先註冊玩家設定
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        ChatColor prefixColor = ChatColor.of(plugin.data.getPrefixColor(p));
        ChatColor nameColor = ChatColor.of(plugin.data.getNameColor(p));
        String customName = plugin.data.getCustomName(p);
        String prefix = plugin.data.getPrefix(p);
        String prefixAndDisplayName = null;

        if(customName == null && prefixColor == null){
            prefixAndDisplayName = prefix + nameColor + name;
        }

        if(customName == null && prefixColor != null){
            prefixAndDisplayName = prefixColor + prefix + nameColor + name;
        }

        if(customName != null && prefixColor == null){
            prefixAndDisplayName = prefix + nameColor + customName;
        }

        if(customName != null && prefixColor != null){
            prefixAndDisplayName = prefixColor + prefix + nameColor + customName + ChatColor.GRAY + ": " + ChatColor.RESET;
        }


        //發送訊息
        if(urls.isEmpty()){

            TextComponent displayMsg = new TextComponent();
            displayMsg.setText(prefixAndDisplayName + rawMsg);
            displayMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT));

        }

        //含有網址
        if(!urls.isEmpty()){

            int indexOfUrl = urls.size();

            //網址設量限制
            if(indexOfUrl > 1){
                if (!p.isOnline()){
                    return;
                }
                p.getPlayer().sendMessage(ChatColor.RED + "你不能同時傳送2個或以上的網址!" +
                        ChatColor.GRAY + "(系統偵測到" + ChatColor.RED + indexOfUrl + ChatColor.GRAY + "個)");
                return;
            }


            TextComponent displayMsg = new TextComponent();
            displayMsg.setText(prefixAndDisplayName + rawMsg);
            displayMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "點我開啟網址\n" + urls.get(0)).create()));
            displayMsg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urls.get(0)));

        }




    }

    public List<String> getUrls(String rawMsg){
        //網址檢查
        String regex = "\\b((?:https?|ftp|file):"
                + "//[-a-zA-Z0-9+&@#/%?="
                + "~_|!:, .;]*[-a-zA-Z0-9+"
                + "&@#/%=~_|])";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern. matcher(rawMsg);

        List<String> url = new ArrayList<>();

        while (matcher.find()){
            url.add(rawMsg.substring(matcher.start(), matcher.end()));
        }
        return url;
    }
}
