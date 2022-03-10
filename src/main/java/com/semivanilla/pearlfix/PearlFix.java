package com.semivanilla.pearlfix;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class PearlFix extends JavaPlugin implements Listener {
    private static List<String> messages = new ArrayList<>();
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        getServer().getPluginManager().registerEvents(this, this);
        messages.clear();
        messages.add("<red>You cannot throw a pearl through a door!");
        //messages.addAll(getConfig().getStringList("messages"));
    }

    @EventHandler
    public void onPearlLand(ProjectileHitEvent event) {
        if (event.getHitBlock() != null && event.getEntityType() == EntityType.ENDER_PEARL) {
            Block hit = event.getHitBlock();
            //check if there is a door/trapdoor in a radius of 1 block around the hit block
            Material type = event.getHitBlock().getType();
            if (type.name().toLowerCase().contains("door")) {
                event.setCancelled(true);
                if (event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player) {
                    Player player = (Player) event.getEntity().getShooter();
                    for (String message : messages) {
                        player.sendMessage(miniMessage.deserialize(message));
                    }
                }
                event.getEntity().remove();
            }
            else {
                Block[] blocks = {
                        hit.getRelative(0, -1, 0),
                        hit.getRelative(0, 1, 0),
                        hit.getRelative(0, 0, -1),
                        hit.getRelative(0, 0, 1),
                        hit.getRelative(-1, 0, 0),
                        hit.getRelative(1, 0, 0)
                };
                for (Block block : blocks) {
                    Material type2 = block.getType();
                    if (type2.name().toLowerCase().contains("door")) {
                        event.setCancelled(true);
                        if (event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player) {
                            Player player = (Player) event.getEntity().getShooter();
                            for (String message : messages) {
                                player.sendMessage(miniMessage.deserialize(message));
                            }
                        }
                        event.getEntity().remove();
                    }
                }
            }
        }
    }
}
