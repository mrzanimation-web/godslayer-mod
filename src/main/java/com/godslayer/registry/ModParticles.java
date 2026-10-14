package com.godslayer.registry;

import com.godslayer.GodSlayerMod;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {
    public static SimpleParticleType BREATHING_SLASH;
    public static SimpleParticleType WATER_DRAGON;
    public static SimpleParticleType FLAME_TIGER;
    public static SimpleParticleType THUNDER_FLASH;
    public static void register(){
        BREATHING_SLASH = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(GodSlayerMod.MOD_ID,"breathing_slash"), FabricParticleTypes.simple());
        WATER_DRAGON = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(GodSlayerMod.MOD_ID,"water_dragon"), FabricParticleTypes.simple());
        FLAME_TIGER = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(GodSlayerMod.MOD_ID,"flame_tiger"), FabricParticleTypes.simple());
        THUNDER_FLASH = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(GodSlayerMod.MOD_ID,"thunder_flash"), FabricParticleTypes.simple());
    }
}
