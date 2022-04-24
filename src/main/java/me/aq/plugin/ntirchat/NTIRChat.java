package me.aq.plugin.ntirchat;

import me.aq.plugin.ntirchat.Data.MySQL;
import me.aq.plugin.ntirchat.Data.SQLeditor;
import me.aq.plugin.ntirchat.Discord.DiscordWebhook;
import me.aq.plugin.ntirchat.Event.Chat;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.TimeZone;

public final class NTIRChat extends JavaPlugin {

    //getPlugin
    private static NTIRChat plugin;
    public static NTIRChat getPlugin(){
        return plugin;
    }

    //SQL
    public MySQL SQL;
    public SQLeditor data;

    //Discord
    public DiscordWebhook webhook;
    public JDA jda;
    String token = "OTI3MDAxOTM2MjE2MDkyNzQy.YdD31A.0eq_ZcNgLQkOG43nix_UgRUT-l4";
    String url = "https://discordapp.com/api/webhooks/956882137141899265/EmWao3-wZvtljIH9opRznh5JFDfsLtEiyolw-8dV5GW-0n0rAhhVyfHSCArXRm1IAXql";




    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

        //資料庫
        SQL = new MySQL();
        data = new SQLeditor();
        try {
            SQL.connect();

        }catch (ClassNotFoundException |SQLException e){
            e.printStackTrace();;
        }

        //Discord機器人
        this.webhook = new DiscordWebhook(url);
        try {
            jda = JDABuilder.createDefault(token).build().awaitReady();
        }catch (LoginException | InterruptedException e){
            if(getConfig().getBoolean("Testing")){
                e.printStackTrace();
            }
        }

        //Event
        getServer().getPluginManager().registerEvents(new Chat(),this);


        //指令


        //PluginMessage
        getServer().getMessenger().registerOutgoingPluginChannel(this,"ntir:chat");



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SQL.disconnect();
        jda.shutdownNow();
    }
}
