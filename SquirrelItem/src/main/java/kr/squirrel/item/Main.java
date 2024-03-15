package kr.squirrel.item;

import kr.squirrel.item.command.SquirrelCommand;
import kr.squirrel.item.listener.ItemHandleListener;
import kr.squirrel.item.object.SquirrelItem;
import kr.squirrel.item.object.skill.PotionSkill;
import kr.squirrel.item.object.skill.Skill;
import kr.squirrel.item.util.NBTUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new ItemHandleListener(), this);
        SquirrelCommand.register();
        SquirrelItem.read();

        for (Player player : Bukkit.getOnlinePlayers()) {
            SquirrelItem.register(player);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getItemInHand() == null) {
                        return;
                    }
                    ItemStack hand = player.getItemInHand();
                    if (!NBTUtil.hasTag(hand, SquirrelItem.ITEM_NBT)) {
                        return;
                    }
                    String tag = NBTUtil.getTag(hand, SquirrelItem.ITEM_NBT);
                    if (!SquirrelItem.exists(tag)) {
                        return;
                    }
                    SquirrelItem squirrelItem = SquirrelItem.get(tag);
                    if (squirrelItem.getSkill() == null) {
                        return;
                    }
                    Skill skill = squirrelItem.getSkill();
                    if (skill instanceof PotionSkill) {
                        PotionSkill potionSkill = (PotionSkill) skill;
                        if (potionSkill.getCooldown() == 0) {
                            player.addPotionEffect(potionSkill.getPotion());
                        }
                    }
                }
            }
        }.runTaskTimer(this, 20, 20);
    }

    @Override
    public void onDisable() {
    }
}
