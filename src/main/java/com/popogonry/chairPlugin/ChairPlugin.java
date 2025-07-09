package com.popogonry.chairPlugin;

import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ChairPlugin extends JavaPlugin {

    private static ChairPlugin instance;
    public static final Map<UUID, ArmorStand> seatedPlayers = new HashMap<>();


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        this.getCommand("앉기").setExecutor(new ChairCommand());
        getServer().getPluginManager().registerEvents(new ChairEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // 남은 아머스탠드 정리
        for (ArmorStand stand : seatedPlayers.values()) {
            if (stand != null && !stand.isDead()) stand.remove();
        }
        seatedPlayers.clear();
    }

    public static ChairPlugin getInstance() {
        return instance;
    }
}
