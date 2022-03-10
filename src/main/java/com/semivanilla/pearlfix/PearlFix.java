package com.semivanilla.pearlfix;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
                return;
            }
            Block above = hit.getRelative(BlockFace.UP);
            Block[] blocks0 = {
                    above.getRelative(BlockFace.NORTH),
                    above.getRelative(BlockFace.SOUTH),
                    above.getRelative(BlockFace.EAST),
                    above.getRelative(BlockFace.WEST),
                    hit.getRelative(BlockFace.DOWN),
                    hit.getRelative(BlockFace.UP),
                    hit.getRelative(BlockFace.NORTH),
                    hit.getRelative(BlockFace.SOUTH),
                    hit.getRelative(BlockFace.EAST),
                    hit.getRelative(BlockFace.WEST),
                    hit.getRelative(BlockFace.NORTH_EAST),
                    hit.getRelative(BlockFace.NORTH_WEST),
                    hit.getRelative(BlockFace.SOUTH_EAST),
                    hit.getRelative(BlockFace.SOUTH_WEST),
                    hit.getRelative(BlockFace.NORTH_NORTH_EAST),
                    hit.getRelative(BlockFace.NORTH_NORTH_WEST),
                    hit.getRelative(BlockFace.SOUTH_SOUTH_EAST),
                    hit.getRelative(BlockFace.SOUTH_SOUTH_WEST),
                    hit.getRelative(BlockFace.EAST_NORTH_EAST),
                    hit.getRelative(BlockFace.EAST_SOUTH_EAST),
                    hit.getRelative(BlockFace.WEST_NORTH_WEST),
                    hit.getRelative(BlockFace.WEST_SOUTH_WEST),
            };
            for (Block block : blocks0) {
                if (block.getType().name().toLowerCase().contains("door")) {
                    event.setCancelled(true);
                    if (event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player) {
                        Player player = (Player) event.getEntity().getShooter();
                        for (String message : messages) {
                            player.sendMessage(miniMessage.deserialize(message));
                        }
                    }
                    event.getEntity().remove();
                    return;
                }
            }
        }
    }
}
