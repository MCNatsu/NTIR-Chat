package me.aq.plugin.ntirchat.Data;

import me.aq.plugin.ntirchat.NTIRChat;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLeditor {

    private NTIRChat plugin = NTIRChat.getPlugin();

    public void createTable(){
        PreparedStatement ps;
        PreparedStatement ps1;
        PreparedStatement ps2;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS PlayerChatSettings"
                    + "(Player VARCHAR(100), UUID VARCHAR(100),Prefix VARCHAR(100) , ShowOnlineMSG BOOLEAN, CustomName VARCHAR(100), NameColor VARCHAR(100), PrefixColor VARCHAR(100),PRIMARY KEY(UUID))");
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //玩家初始設定
    public void createPlayer(Player p){
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("INSERT INTO PlayerChatSettings (Player,UUID,Prefix,ShowOnlineMSG,CustomName,NameColor,PrefixColor) VALUE(?,?,?,?,?,?,?)");
            ps.setString(1,p.getName());
            ps.setString(2,p.getUniqueId().toString());
            ps.setString(3, ChatColor.AQUA + "玩家" + ChatColor.RESET);
            ps.setBoolean(4,true);
            ps.setString(5,null);
            ps.setString(6,null);
            ps.setString(7,null);

            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean chatSettingExist(Player p){
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("SELECT UUID FROM PlayerChatSettings WHERE UUID=?)");
            ps.setString(1,p.getUniqueId().toString());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //玩家聊天設定(玩家發送訊息)
    public String getPrefix(Player p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT Prefix FROM PlayerChatSettings WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("prefix");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getPrefixColor(Player p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT PrefixColor FROM PlayerChatSettings WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("PrefixColor");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getNameColor(Player p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT NameColor FROM PlayerChatSettings WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("NameColor");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getCustomName(Player p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT CustomName FROM PlayerChatSettings WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("CustomName");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    //玩家聊天訊息(玩家接收訊息)
    public List<String> getIgnoredPlayer(Player p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT IgnoredPlayerUUID FROM PlayerReceiveSetting WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            ArrayList<String> IgnoredPlayer = new ArrayList<>();
            for (int cur = 0; cur < ignoredPlayerCount(p);cur++ ,rs.next()){
                IgnoredPlayer.add(rs.getString("IgnoredPlayerUUID"));

            }
            return IgnoredPlayer;
        }catch (SQLException e){
            if(plugin.getConfig().getBoolean("Testing")){
                return null;
            }
            e.printStackTrace();
        }
        return null;
    }

    public int ignoredPlayerCount(Player p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT COUNT(IgnoredPlayerUUID) AS COUNT FROM PlayerReceiveSetting WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("COUNT");
            }
        }catch (SQLException e){
            if(plugin.getConfig().getBoolean("Testing")){
                return 0;
            }
            e.printStackTrace();
        }
        return 0;
    }


    //玩家聊天訊息(系統訊息)
    public String getOnlineMessageFormat(Player p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT OnlineMessage FROM PlayerSystemSetting WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("OnlineMessage");
            }
        }catch (SQLException e){
            if(plugin.getConfig().getBoolean("Testing")){
                return null;
            }
            e.printStackTrace();
        }
        return null;
    }

    public Boolean showDeathMessage(Player p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT ShowDeathMessage FROM PlayerSystemSetting WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("ShowDeathMessage");
            }
        }catch (SQLException e){
            if(plugin.getConfig().getBoolean("Testing")){
                return null;
            }
            e.printStackTrace();
        }
        return null;
    }
}
