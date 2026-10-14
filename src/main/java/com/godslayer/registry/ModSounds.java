package com.godslayer.registry;

import com.godslayer.GodSlayerMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static SoundEvent KATANA_SLASH;
    public static SoundEvent BREATHING_ACTIVATE;
    public static SoundEvent GOD_PULSE;
    public static void register(){
        KATANA_SLASH = reg("katana_slash");
        BREATHING_ACTIVATE = reg("breathing_activate");
        GOD_PULSE = reg("god_pulse");
    }
    private static SoundEvent reg(String name){
        Identifier id = Identifier.of(GodSlayerMod.MOD_ID, name);
        SoundEvent ev = SoundEvent.of(id);
        return Registry.register(Registries.SOUND_EVENT, id, ev);
    }
}
