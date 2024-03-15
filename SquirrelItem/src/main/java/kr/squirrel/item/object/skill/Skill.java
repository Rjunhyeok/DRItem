package kr.squirrel.item.object.skill;

import kr.squirrel.item.Main;
import kr.squirrel.item.object.SquirrelItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class Skill {

    private int cooldown;
    private String itemName;

    public Skill(int cooldown, String itemName) {
        this.cooldown = cooldown;
        this.itemName = itemName;
    }

    public void cast(Player player) {
        Skill.makeCooldown(player, itemName, cooldown);
    }

    public boolean canCast(Player player) {
        if (!SquirrelItem.isAvailableCasting(player, itemName)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§c쿨타임이 " + (cooldown - ChronoUnit.SECONDS.between(SquirrelItem.getCooldownMap(player.getUniqueId().toString()).get(itemName), LocalDateTime.now())) + "초 남았습니다"));
            return false;
        }
        return true;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String getItemName() {
        return itemName;
    }

    protected static void makeCooldown(Player player, String itemName, int cooldown) {
        SquirrelItem.getCooldownMap(player.getUniqueId().toString()).put(itemName, LocalDateTime.now());
        new BukkitRunnable() {
            @Override
            public void run() {
                SquirrelItem.getCooldownMap(player.getUniqueId().toString()).remove(itemName);
            }
        }.runTaskLater(Main.getInstance(), 20 * cooldown);
    }
}