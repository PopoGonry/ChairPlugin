package com.popogonry.chairPlugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChairCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("플레이어만 사용할 수 있습니다.");
            return true;
        }

        UUID uuid = player.getUniqueId();

        if (ChairPlugin.seatedPlayers.containsKey(uuid)) {
            player.sendMessage("이미 앉아 있습니다.");
            return true;
        }


        double y = 0;
        if(strings.length > 0) {
            try {
                y = Double.parseDouble(strings[0]);
            } catch (Exception e) {

            }
        }

        Location loc = player.getLocation().clone();
        loc.setY(loc.getY() - y); // 앉은 높이 보정
        loc.setPitch(0); // 고개 들기
        loc.setYaw(player.getLocation().getYaw()); // 플레이어 바라보는 방향 유지

        ArmorStand chair = loc.getWorld().spawn(loc, ArmorStand.class, as -> {
            as.setVisible(false);
            as.setMarker(true);
            as.setGravity(false);
            as.setSmall(true);
            as.setRotation(loc.getYaw(), 0); // 방향 반영
        });

        chair.addPassenger(player);
        ChairPlugin.seatedPlayers.put(uuid, chair);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!ChairPlugin.seatedPlayers.containsKey(player.getUniqueId())) {
                    cancel();
                    return;
                }

                ArmorStand stand = ChairPlugin.seatedPlayers.get(player.getUniqueId());
                if (stand == null || stand.isDead()) {
                    cancel();
                    return;
                }

                float yaw = player.getLocation().getYaw();
                stand.setRotation(yaw, 0); // yaw: 좌우 회전, pitch는 필요 없음
            }
        }.runTaskTimer(ChairPlugin.getInstance(), 0L, 2L); // 0.1초마다 갱신
        return true;
    }
}
