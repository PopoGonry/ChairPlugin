package com.popogonry.chairPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChairEvent implements Listener {


    // 쉬프트로 일어나면 제거
    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!ChairPlugin.seatedPlayers.containsKey(uuid)) return;

        // 쉬프트 누름으로 인한 하차
        ArmorStand stand = ChairPlugin.seatedPlayers.remove(uuid);
        Bukkit.getScheduler().runTaskLater(ChairPlugin.getInstance(), () -> {
            if (stand != null && !stand.isDead()) {
                stand.remove();
            }
        }, 1L); // 다음 틱에 제거 (안전하게)
    }

}
