package kr.squirrel.item.command;

import com.google.common.collect.Lists;
import kr.squirrel.item.libs.SimpleCommandBuilder;
import kr.squirrel.item.object.SquirrelItem;
import kr.squirrel.item.object.skill.CommandSkill;
import kr.squirrel.item.object.skill.PotionSkill;
import kr.squirrel.item.object.skill.TeleportSkill;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SquirrelCommand {

    public static void register() {
        new SimpleCommandBuilder("다람쥐")
                .aliases("squirrel")
                .permission("op")
                .commandExecutor((sender, command, label, args) -> {
                            if (sender instanceof Player player) {
                                if (args.length == 0) {
                                    player.sendMessage("/다람쥐 만들기 [name]");
                                    player.sendMessage("/다람쥐 목록");
                                    player.sendMessage("/다람쥐 아이템 [name]");
                                    player.sendMessage("/다람쥐 이름 [name] [displayName]");
                                    player.sendMessage("/다람쥐 로어등록 [name]");
                                    player.sendMessage("/다람쥐 로어삭제 [name]");
                                    player.sendMessage("/다람쥐 지급 [name]");
                                    player.sendMessage("/다람쥐 능력부여 [name] 포션효과 [potionName] [potionGrade] [duration] [cooldown]");
                                    player.sendMessage("/다람쥐 능력부여 [name] 텔레포트 [blink_count] [cooldown]");
                                    player.sendMessage("/다람쥐 능력제거 [name]");
                                    player.sendMessage("/다람쥐 삭제 [name]");
                                    return false;
                                }
                                switch (args[0]) {
                                    case "만들기" -> {
                                        String name = args[1];
                                        if (SquirrelItem.exists(name)) {
                                            player.sendMessage("§c이미 존재합니다");
                                            return false;
                                        }
                                        SquirrelItem.create(name);
                                        player.sendMessage("생성 완료");
                                        return false;
                                    }
                                    case "목록" -> {
                                        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "[ 목록 ]");
                                        List<ItemStack> allItems = SquirrelItem.allItems();
                                        for (int i = 0; i < allItems.size(); i++) {
                                            inventory.setItem(i, allItems.get(i));
                                        }
                                        player.openInventory(inventory);
                                        return false;
                                    }
                                    case "아이템" -> {
                                        String name = args[1];
                                        if (!SquirrelItem.exists(name)) {
                                            player.sendMessage("§c존재하지 않습니다");
                                            return false;
                                        }
                                        ItemStack hand = player.getItemInHand();
                                        if (hand == null) {
                                            player.sendMessage("아이템을 들고 입력해주세요");
                                            return false;
                                        }
                                        SquirrelItem squirrelItem = SquirrelItem.get(name);
                                        squirrelItem.setMaterial(hand.getType().name());
                                        if (hand.hasItemMeta()) {
                                            squirrelItem.setCustomModelData(hand.getItemMeta().getCustomModelData());
                                        }
                                        squirrelItem.save();
                                        player.sendMessage("설정 완료");
                                        return false;
                                    }
                                    case "이름" -> {
                                        String name = args[1];
                                        if (!SquirrelItem.exists(name)) {
                                            player.sendMessage("§c존재하지 않습니다");
                                            return false;
                                        }
                                        SquirrelItem squirrelItem = SquirrelItem.get(name);
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 2; i < args.length; i++) {
                                            sb.append(" " + args[i].replaceAll("&", "§"));
                                        }
                                        String displayName = sb.toString().trim();
                                        squirrelItem.setDisplayName(displayName);
                                        squirrelItem.save();
                                        player.sendMessage("설정 완료");
                                        return false;
                                    }
                                    case "로어등록" -> {
                                        String name = args[1];
                                        if (!SquirrelItem.exists(name)) {
                                            player.sendMessage("§c존재하지 않습니다");
                                            return false;
                                        }
                                        SquirrelItem squirrelItem = SquirrelItem.get(name);
                                        List<String> lore = squirrelItem.getLore();
                                        if (lore == null) {
                                            lore = Lists.newArrayList();
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 2; i < args.length; i++) {
                                            sb.append(" " + args[i].replaceAll("&", "§"));
                                        }
                                        lore.add(sb.toString().trim());
                                        squirrelItem.setLore(lore);
                                        squirrelItem.save();
                                        player.sendMessage("설정 완료");
                                        return false;
                                    }
                                    case "로어삭제" -> {
                                        String name = args[1];
                                        if (!SquirrelItem.exists(name)) {
                                            player.sendMessage("§c존재하지 않습니다");
                                            return false;
                                        }
                                        SquirrelItem squirrelItem = SquirrelItem.get(name);
                                        List<String> lore = squirrelItem.getLore();
                                        if (lore == null) {
                                            player.sendMessage("§c로어가 없습니다");
                                            return false;
                                        }
                                        lore.remove(lore.size() - 1);
                                        squirrelItem.setLore(lore);
                                        squirrelItem.save();
                                        player.sendMessage("설정 완료");
                                        return false;
                                    }
                                    case "지급" -> {
                                        String name = args[1];
                                        if (!SquirrelItem.exists(name)) {
                                            player.sendMessage("§c존재하지 않습니다");
                                            return false;
                                        }
                                        player.getInventory().addItem(SquirrelItem.get(name).getItem());
                                        player.sendMessage("설정 완료");
                                        return false;
                                    }
                                    case "능력부여" -> {
                                        String name = args[1];
                                        if (!SquirrelItem.exists(name)) {
                                            player.sendMessage("§c존재하지 않습니다");
                                            return false;
                                        }
                                        SquirrelItem squirrelItem = SquirrelItem.get(name);
                                        switch (args[2]) {
                                            case "포션효과" -> {
                                                if (args.length == 5) {
                                                    PotionSkill potionSkill = new PotionSkill(name, 0, args[3], Integer.parseInt(args[4]), 1);
                                                    squirrelItem.setSkill(potionSkill);
                                                    squirrelItem.save();
                                                    player.sendMessage("설정 완료");
                                                    return false;
                                                }
                                                PotionSkill potionSkill = new PotionSkill(name, Integer.parseInt(args[6]), args[3], Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                                squirrelItem.setSkill(potionSkill);
                                                squirrelItem.save();
                                                player.sendMessage("설정 완료");
                                            }
                                            case "텔레포트" -> {
                                                TeleportSkill teleportSkill = new TeleportSkill(name, Integer.parseInt(args[4]), Integer.parseInt(args[3]));
                                                squirrelItem.setSkill(teleportSkill);
                                                squirrelItem.save();
                                                player.sendMessage("설정 완료");
                                            }
                                            case "명령어" -> {
                                                StringBuilder sb = new StringBuilder();
                                                for (int i = 4; i < args.length; i++) {
                                                    sb.append(" " + args[i]);
                                                }
                                                CommandSkill commandSkill = new CommandSkill(name, Integer.parseInt(args[3]), sb.toString().trim());
                                                squirrelItem.setSkill(commandSkill);
                                                squirrelItem.save();
                                                player.sendMessage("설정 완료");
                                            }
                                        }
                                        return false;
                                    }
                                    case "능력제거" -> {
                                        String name = args[1];
                                        if (!SquirrelItem.exists(name)) {
                                            player.sendMessage("§c존재하지 않습니다");
                                            return false;
                                        }
                                        SquirrelItem squirrelItem = SquirrelItem.get(name);
                                        squirrelItem.setSkill(null);
                                        squirrelItem.save();
                                        player.sendMessage("설정 완료");
                                    }
                                    case "삭제" -> {
                                        String name = args[1];
                                        if (!SquirrelItem.exists(name)) {
                                            player.sendMessage("§c존재하지 않습니다");
                                            return false;
                                        }
                                        SquirrelItem.remove(name);
                                        player.sendMessage("삭제 완료");
                                    }
                                }
                            }
                            return false;
                        }
                ).tabCompleter((sender, command, label, args) -> {
                    if (sender instanceof Player) {
                        switch (args.length) {
                            case 1 -> {
                                return List.of("만들기", "목록", "아이템", "이름", "로어등록", "로어삭제", "지급", "능력부여", "능력제거", "삭제");
                            }
                            case 2 -> {
                                return SquirrelItem.getAllNames();
                            }
                            case 4 -> {
                                if (args[2].equalsIgnoreCase("포션효과")) {
                                    return Arrays.stream(PotionEffectType.values()).map(PotionEffectType::getName).collect(Collectors.toList());
                                }
                            }
                        }
                    }
                    return null;
                }).register();
    }

}
