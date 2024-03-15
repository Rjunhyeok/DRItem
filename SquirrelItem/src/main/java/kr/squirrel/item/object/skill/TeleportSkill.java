package kr.squirrel.item.object.skill;

import kr.squirrel.item.object.SquirrelItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TeleportSkill extends Skill {

    private int blinkBlock;

    public TeleportSkill(String itemName, int cooldown, int blinkBlock) {
        super(cooldown, itemName);
        this.blinkBlock = blinkBlock;
    }

    @Override
    public void cast(Player player) {
        if (super.canCast(player)) {
            super.cast(player);
            Location location = player.getLocation();
            Vector direction = location.getDirection();
            direction.normalize();
            direction.multiply(blinkBlock);
            location.add(direction);
            player.teleport(location);
            String name = SquirrelItem.get(getItemName()).getDisplayName();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((name == null ? "N" : name) + "§a의 능력을 사용하였습니다!"));
        }
    }

    public int getBlinkBlock() {
        return blinkBlock;
    }
}
