package com.novinitygames.clientIDServer.utils;

import com.novinitygames.clientIDServer.ClientIDServer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class LuckPermsUtils {
    
    private static LuckPerms luckPerms;
    
    public static boolean isLuckPermsAvailable() {
        if (luckPerms == null) {
            try {
                luckPerms = LuckPermsProvider.get();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean hasBypassRole(Player player) {
        if (!isLuckPermsAvailable()) {
            return false;
        }
        
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return false;
        }
        
        List<String> bypassRoles = ClientIDServer.getInstance().getConfig()
            .getStringList("luckPermsBypassRoles");
        
        for (String role : bypassRoles) {
            if (user.getCachedData().getMetaData().getPrefix() != null && 
                user.getCachedData().getMetaData().getPrefix().contains(role)) {
                return true;
            }
            
            if (user.getInheritedGroups(QueryOptions.defaultContextualOptions()).stream()
                .anyMatch(group -> group.getName().equalsIgnoreCase(role))) {
                return true;
            }
        }
        
        return false;
    }
}