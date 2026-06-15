package com.github.Glatinis.borderedSMP.revive;

import com.github.Glatinis.borderedSMP.life.LifeManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ReviveManager {

    private final JavaPlugin plugin;
    private final ReviveConfig config;
    private final LifeManager lifeManager;
    private final NamespacedKey reviveItemKey;
    private final NamespacedKey recipeKey;
    private final Set<UUID> pendingRevives = new HashSet<>();

    public ReviveManager(JavaPlugin plugin, ReviveConfig config, LifeManager lifeManager) {
        this.plugin = plugin;
        this.config = config;
        this.lifeManager = lifeManager;
        this.reviveItemKey = new NamespacedKey(plugin, "revive_item");
        this.recipeKey = new NamespacedKey(plugin, "revive_recipe");
    }

    public ItemStack createReviveItem() {
        Material material = Material.matchMaterial(config.getResultMaterial());
        if (material == null) material = Material.TOTEM_OF_UNDYING;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Revive Token"));
        meta.getPersistentDataContainer().set(reviveItemKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    public boolean isReviveItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta()
                   .getPersistentDataContainer()
                   .has(reviveItemKey, PersistentDataType.BOOLEAN);
    }

    public void registerRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, createReviveItem());

        List<String> shape = config.getShape();
        if (shape.size() == 3) {
            recipe.shape(shape.get(0), shape.get(1), shape.get(2));
        } else {
            recipe.shape("DGD", "GEG", "DGD");
        }

        for (Map.Entry<String, Object> entry : config.getIngredients().entrySet()) {
            char key = entry.getKey().charAt(0);
            Material mat = Material.matchMaterial(entry.getValue().toString());
            if (mat != null) recipe.setIngredient(key, mat);
        }

        Bukkit.addRecipe(recipe);
    }

    public void reloadRecipe() {
        Bukkit.removeRecipe(recipeKey);
        registerRecipe();
    }

    public boolean isPendingRevive(UUID uuid) {
        return pendingRevives.contains(uuid);
    }

    public void initiateRevive(Player player) {
        if (!config.isEnabled()) {
            player.sendMessage("The revive system is currently disabled.");
            return;
        }
        pendingRevives.add(player.getUniqueId());
        player.sendMessage("Type the name of the player you want to revive in chat.");
    }

    @SuppressWarnings("deprecation")
    public void completeRevive(Player initiator, String targetName) {
        pendingRevives.remove(initiator.getUniqueId());

        if (!config.isEnabled()) {
            initiator.sendMessage("The revive system is currently disabled.");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (!target.hasPlayedBefore()) {
            initiator.sendMessage("Player '" + targetName + "' has never joined this server.");
            return;
        }

        UUID targetUuid = target.getUniqueId();
        if (lifeManager.getLives(targetUuid) > 0) {
            initiator.sendMessage(targetName + " does not need to be revived.");
            return;
        }

        ItemStack item = initiator.getInventory().getItemInMainHand();
        if (!isReviveItem(item)) {
            initiator.sendMessage("You no longer have the revive item in your hand.");
            return;
        }

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            initiator.getInventory().setItemInMainHand(null);
        }

        lifeManager.setLives(targetUuid, targetName, config.getLivesToRestore());
        initiator.sendMessage("You have revived " + targetName + " with " + config.getLivesToRestore() + " lives.");
    }
}
