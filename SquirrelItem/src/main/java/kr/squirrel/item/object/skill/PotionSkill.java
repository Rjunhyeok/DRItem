package kr.squirrel.item.object.skill;

import kr.squirrel.item.object.SquirrelItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionSkill extends Skill {
    private String effect;
    private int amplifier;
    private int duration;

    public PotionSkill(String itemName, int cooldown, String effect, int amplifier, int duration) {
        super(cooldown, itemName);
        this.effect = effect;
        this.amplifier = amplifier;
        this.duration = duration;
    }

    @Override
    public void cast(Player player) {
        if (getCooldown() == 0) {
            return;
        }
        if (super.canCast(player)) {
            super.cast(player);
            player.addPotionEffect(getPotion());
            String name = SquirrelItem.get(getItemName()).getDisplayName();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((name == null ? "N" : name) + "§a의 능력을 사용하였습니다!"));
        }
    }

    public PotionEffect getPotion() {
        return new PotionEffect(PotionEffectType.getByName(effect), duration * 20, amplifier);
    }

    @Override
    public int getCooldown() {
        return super.getCooldown();
    }

    public String getEffect() {
        return effect;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public int getDuration() {
        return duration;
    }
}
