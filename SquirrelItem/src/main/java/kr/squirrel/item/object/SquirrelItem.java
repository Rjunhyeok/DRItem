package kr.squirrel.item.object;

import com.google.common.collect.Maps;
import kr.squirrel.item.Main;
import kr.squirrel.item.object.skill.CommandSkill;
import kr.squirrel.item.object.skill.PotionSkill;
import kr.squirrel.item.object.skill.Skill;
import kr.squirrel.item.object.skill.TeleportSkill;
import kr.squirrel.item.util.NBTUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SquirrelItem implements Save {


    public static final String ITEM_NBT = "squirrel_item:item";

    private static final File DATA_DIRECTORY = new File(Main.getInstance().getDataFolder(), "items");
    private static final Map<String, SquirrelItem> customItems = Maps.newHashMap();

    private static final Map<String, Map<String, LocalDateTime>> cooltimes = Maps.newHashMap();

    public static SquirrelItem get(String name) {
        return customItems.get(name);
    }

    public static boolean exists(String name) {
        return customItems.containsKey(name);
    }

    public static boolean isAvailableCasting(Player player, String name) {
        return cooltimes.get(player.getUniqueId().toString()).containsKey(name) ? false : true;
    }

    public static Map<String, LocalDateTime> getCooldownMap(String uuid) {
        return cooltimes.get(uuid);
    }

    public static void create(String name) {
        SquirrelItem squirrelItem = new SquirrelItem(name);
        squirrelItem.save();
        customItems.put(name, squirrelItem);
    }

    public static void register(Player player) {
        cooltimes.put(player.getUniqueId().toString(), Maps.newHashMap());
    }


    public static List<ItemStack> allItems() {
        return customItems.values().stream().map(SquirrelItem::getItem).collect(Collectors.toList());
    }

    public static List<String> getAllNames() {
        return customItems.keySet().stream().toList();
    }

    public static void remove(String name) {
        SquirrelItem squirrelItem = get(name);
        squirrelItem.remove();
        customItems.remove(name);
    }

    public static void read() {
        if (DATA_DIRECTORY.listFiles() == null) {
            return;
        }
        for (File file : DATA_DIRECTORY.listFiles()) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            String name = yaml.getString("name");
            String material = yaml.getString("material");
            String displayName = yaml.getString("displayName");
            List<String> lore = yaml.getStringList("lore");
            int customModelData = yaml.getInt("customModelData");
            Skill skill = null;
            if (yaml.get("skill.type") != null) {
                String type = yaml.getString("skill.type");
                int cooldown = yaml.getInt("skill.cooldown");
                switch (type) {
                    case "CommandSkill" -> {
                        skill = new CommandSkill(name, cooldown, yaml.getString("skill.command"));
                    }
                    case "PotionSkill" -> {
                        skill = new PotionSkill(name, cooldown, yaml.getString("skill.effect"), yaml.getInt("skill.amplifier"), yaml.getInt("skill.duration"));
                    }
                    case "TeleportSkill" -> {
                        skill = new TeleportSkill(name, cooldown, yaml.getInt("skill.block"));
                    }
                }
            }
            SquirrelItem squirrelItem = new SquirrelItem(name, material, displayName, lore, customModelData, skill);
            customItems.put(name, squirrelItem);
        }
    }

    private String name;
    private String material;
    private String displayName;
    private List<String> lore;
    private int customModelData;
    private Skill skill;

    public SquirrelItem(String name) {
        this.name = name;
    }

    public SquirrelItem(String name, String material, String displayName, List<String> lore, int customModelData, Skill skill) {
        this.name = name;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.customModelData = customModelData;
        this.skill = skill;
    }

    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.getMaterial(material));
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (displayName != null) {
            itemMeta.setDisplayName(displayName);

        }
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);
        itemStack = NBTUtil.putTag(itemStack, ITEM_NBT, name);
        return itemStack;
    }

    @Override
    public void save() {
        final File FILE = new File(DATA_DIRECTORY, name + ".yml");
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("name", name);
        yaml.set("material", material);
        yaml.set("displayName", displayName);
        yaml.set("lore", lore);
        yaml.set("customModelData", customModelData);
        if (skill != null) {
            yaml.set("skill.cooldown", skill.getCooldown());
            if (skill instanceof CommandSkill) {
                yaml.set("skill.type", "CommandSkill");
                yaml.set("skill.command", ((CommandSkill) skill).getCommand());
            } else if (skill instanceof PotionSkill) {
                PotionSkill potionSkill = (PotionSkill) skill;
                yaml.set("skill.type", "PotionSkill");
                yaml.set("skill.effect", potionSkill.getEffect());
                yaml.set("skill.amplifier", potionSkill.getAmplifier());
                yaml.set("skill.duration", potionSkill.getDuration());
            } else if (skill instanceof TeleportSkill) {
                TeleportSkill teleportSkill = (TeleportSkill) skill;
                yaml.set("skill.type", "TeleportSkill");
                yaml.set("skill.block", teleportSkill.getBlinkBlock());
            }
            try {
                yaml.save(FILE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void remove() {
        final File FILE = new File(DATA_DIRECTORY, name + ".yml");
        FILE.delete();
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
