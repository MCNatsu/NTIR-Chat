package me.aq.plugin.ntirchat;

import me.aq.plugin.ntirchat.Data.MySQL;
import me.aq.plugin.ntirchat.Data.SQLeditor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.TimeZone;

public final class NTIRChat extends JavaPlugin {

    private static NTIRChat plugin;
    public MySQL SQL;
    public SQLeditor data;
    public static NTIRChat getPlugin(){
        return plugin;
    }
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

        //Event

        //指令

        //PluginMessage



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
