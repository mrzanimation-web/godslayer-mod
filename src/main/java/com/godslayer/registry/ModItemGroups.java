package com.godslayer.registry;

import com.godslayer.GodSlayerMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static ItemGroup GODSLAYER_GROUP;
    public static void register(){
        Identifier id = Identifier.of(GodSlayerMod.MOD_ID, "main");
        RegistryKey<ItemGroup> key = RegistryKey.of(RegistryKeys.ITEM_GROUP, id);
        ItemGroup group = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.GOD_KATANA != null ? ModItems.GOD_KATANA : net.minecraft.item.Items.NETHERITE_SWORD))
            .displayName(Text.literal("⚔ GOD SLAYER ⚔"))
            .entries((ctx, entries) -> {
                for (var e : ModItems.ALL.values()) entries.add(e);
            })
            .build();
        GODSLAYER_GROUP = Registry.register(Registries.ITEM_GROUP, key, group);
    }
}
