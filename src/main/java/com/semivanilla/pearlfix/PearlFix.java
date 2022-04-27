package com.semivanilla.pearlfix;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
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
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

import static org.bukkit.Material.values;

public final class PearlFix extends JavaPlugin implements Listener {
    private static List<String> messages = new ArrayList<>();
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    private static EnumSet<Material> blockedItems;

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
        for (String message : getConfig().getStringList("message")) {
            messages.add(message);
        }
        List<Material> materials = new ArrayList<>();
        for (String blockedItems : getConfig().getStringList("blocked_items")) {
            Pattern pattern = Pattern.compile(blockedItems);
            for (Material value : values()) {
                if (pattern.matcher(value.name()).matches())
                    materials.add(value);
            }
        }
        blockedItems = EnumSet.copyOf(materials);
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
                if (block.isEmpty())
                    continue;
                if (blockedItems.contains(block.getType())) {
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
