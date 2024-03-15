package kr.squirrel.item.listener;

import kr.squirrel.item.object.SquirrelItem;
import kr.squirrel.item.util.NBTUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemHandleListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack hand = player.getItemInHand();
        if (hand == null) {
            return;
        }
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
        squirrelItem.getSkill().cast(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        SquirrelItem.register(event.getPlayer());
    }

}
