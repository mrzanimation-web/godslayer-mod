package com.godslayer.registry;

import com.godslayer.GodSlayerMod;
import com.godslayer.breathing.BreathingStyle;
import com.godslayer.item.GodSwordItem;
import com.godslayer.item.GodToolItem;
import com.godslayer.item.NichirinSwordItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {
    public static final Map<Identifier, Item> ALL = new LinkedHashMap<>();
    public static final Map<BreathingStyle, Item> NICHIRIN_MAP = new LinkedHashMap<>();
    public static Item GOD_KATANA, GOD_GREATSWORD, GOD_SCYTHE, GOD_PICKAXE, GOD_AXE, GOD_SHOVEL, GOD_HOE, GOD_BOW;

    public static void register(){
        GOD_KATANA = regGodSword("god_katana","God Katana - INFINITY EDGE",45);
        GOD_GREATSWORD = regGodSword("god_greatsword","God Greatsword - WORLD CLEAVER",60);
        GOD_SCYTHE = regGodSword("god_scythe","God Scythe - SOUL REAPER",55);
        GOD_PICKAXE = regGodTool("god_pickaxe", GodToolItem.ToolType.PICKAXE,"God Pickaxe - REALITY BREAKER");
        GOD_AXE = regGodTool("god_axe", GodToolItem.ToolType.AXE,"God Axe - WORLD TREE FELLER");
        GOD_SHOVEL = regGodTool("god_shovel", GodToolItem.ToolType.SHOVEL,"God Shovel - EARTH MOVER");
        GOD_HOE = regGodTool("god_hoe", GodToolItem.ToolType.HOE,"God Hoe - HARVEST GOD");
        // God Bow simple
        {
            Identifier id = Identifier.of(GodSlayerMod.MOD_ID,"god_bow");
            RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
            Item item = new Item(new Item.Settings().registryKey(key).maxCount(1).fireproof());
            Item reg = Registry.register(Registries.ITEM, key, item);
            GOD_BOW = reg; ALL.put(id, reg);
        }
        for (BreathingStyle s : BreathingStyle.values()){
            String name = "nichirin_" + s.name().toLowerCase();
            Identifier id = Identifier.of(GodSlayerMod.MOD_ID, name);
            RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
            NichirinSwordItem item = new NichirinSwordItem(s, key);
            Item reg = Registry.register(Registries.ITEM, key, item);
            NICHIRIN_MAP.put(s, reg);
            ALL.put(id, reg);
        }
        GodSlayerMod.LOGGER.info("Registered {} items including {} Nichirin", ALL.size(), NICHIRIN_MAP.size());
    }
    private static Item regGodSword(String name, String godName, int dmg){
        Identifier id = Identifier.of(GodSlayerMod.MOD_ID,name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        GodSwordItem item = new GodSwordItem(new Item.Settings().registryKey(key).maxCount(1).fireproof(), godName, dmg, 0xFFD700);
        // GodSwordItem currently takes Settings without registryKey baked? Re-create properly:
        Item reg = Registry.register(Registries.ITEM, key, item);
        ALL.put(id, reg); return reg;
    }
    private static Item regGodTool(String name, GodToolItem.ToolType type, String label){
        Identifier id = Identifier.of(GodSlayerMod.MOD_ID,name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        GodToolItem item = new GodToolItem(new Item.Settings().registryKey(key), type, label, 50f);
        Item reg = Registry.register(Registries.ITEM, key, item);
        ALL.put(id, reg); return reg;
    }
    public static void giveGodSet(PlayerEntity p){
        p.giveItemStack(new ItemStack(GOD_KATANA)); p.giveItemStack(new ItemStack(GOD_GREATSWORD));
        p.giveItemStack(new ItemStack(GOD_SCYTHE)); p.giveItemStack(new ItemStack(GOD_PICKAXE));
        p.giveItemStack(new ItemStack(GOD_AXE)); p.giveItemStack(new ItemStack(GOD_SHOVEL));
        p.giveItemStack(new ItemStack(GOD_HOE)); p.giveItemStack(new ItemStack(GOD_BOW));
    }
    public static void giveAllNichirin(PlayerEntity p){ for(Item it: NICHIRIN_MAP.values()) p.giveItemStack(new ItemStack(it)); }
}
