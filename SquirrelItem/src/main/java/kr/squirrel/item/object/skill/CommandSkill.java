package kr.squirrel.item.object.skill;

import org.bukkit.entity.Player;

public class CommandSkill extends Skill {

    private String command;

    public CommandSkill(String itemName, int cooldown, String command) {
        super(cooldown, itemName);
        this.command = command;
    }

    @Override
    public void cast(Player player) {
        if (super.canCast(player)) {
            super.cast(player);
            player.performCommand(command);
        }
    }

    public String getCommand() {
        return command;
    }
}
