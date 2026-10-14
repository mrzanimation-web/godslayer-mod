package com.godslayer.breathing;

import com.godslayer.item.NichirinSwordItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.*;

public class BreathingManager {
    private static final Map<UUID, BreathingStyle> PLAYER_STYLE = new HashMap<>();
    private static final Map<UUID, Integer> PLAYER_FORM = new HashMap<>();
    private static final Map<UUID, Long> COOLDOWN = new HashMap<>();
    private static final Map<UUID, Boolean> GOD_MODE = new HashMap<>();

    public static BreathingStyle getStyle(PlayerEntity p){ return PLAYER_STYLE.getOrDefault(p.getUuid(), BreathingStyle.WATER); }
    public static int getForm(PlayerEntity p){ return PLAYER_FORM.getOrDefault(p.getUuid(), 0); }
    public static boolean isGodMode(PlayerEntity p){ return GOD_MODE.getOrDefault(p.getUuid(), false); }

    public static void cycleBreathing(PlayerEntity p, boolean reverse){
        BreathingStyle[] vals = BreathingStyle.values();
        int idx = Arrays.asList(vals).indexOf(getStyle(p));
        idx = reverse ? (idx - 1 + vals.length) % vals.length : (idx + 1) % vals.length;
        BreathingStyle next = vals[idx];
        PLAYER_STYLE.put(p.getUuid(), next);
        PLAYER_FORM.put(p.getUuid(), 0);
        // Auto-switch sword color if holding nichirin
        ItemStack held = p.getMainHandStack();
        if (held.getItem() instanceof NichirinSwordItem) held.setDamage(0);
        p.sendMessage(Text.literal("▬▬▬ Breathing Switched ▬▬▬").formatted(Formatting.DARK_GRAY), true);
        p.sendMessage(Text.literal("▶ " + next.getDisplayName() + " (" + next.getJapanese() + ")").formatted(next.getColor(), Formatting.BOLD), false);
        p.sendMessage(Text.literal("  " + next.getFormName(0)).formatted(Formatting.GRAY), false);
        p.getWorld().playSound(null, p.getBlockPos(), next.getSound(), SoundCategory.PLAYERS, 1f, 1.2f);
        spawnSwitchParticles(p);
    }

    public static void cycleForm(PlayerEntity p, boolean reverse){
        int form = getForm(p);
        BreathingStyle s = getStyle(p);
        form = reverse ? (form -1 + s.getForms().length) % s.getForms().length : (form+1) % s.getForms().length;
        PLAYER_FORM.put(p.getUuid(), form);
        p.sendMessage(Text.literal("◆ Form " + (form+1) + ": " + s.getFormName(form)).formatted(s.getColor(), Formatting.BOLD), true);
        p.getWorld().playSound(null, p.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 1f, 1.5f);
    }

    public static void toggleGodMode(PlayerEntity p){
        boolean v = !isGodMode(p);
        GOD_MODE.put(p.getUuid(), v);
        p.sendMessage(Text.literal(v ? "☀ GOD MODE: ON - YOU ARE A HASHIRA GOD ☀" : "GOD MODE: OFF").formatted(v ? Formatting.GOLD : Formatting.GRAY, Formatting.BOLD), false);
        if (v){ p.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 999999, 4, false, false)); p.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 999999, 2, false, false)); p.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 999999, 1, false, false)); }
        else { p.removeStatusEffect(StatusEffects.RESISTANCE); p.removeStatusEffect(StatusEffects.REGENERATION); p.removeStatusEffect(StatusEffects.SPEED); }
    }

    public static void triggerForm(PlayerEntity p){
        long now = p.getWorld().getTime();
        if (COOLDOWN.getOrDefault(p.getUuid(), 0L) > now){ int cd = (int)(COOLDOWN.get(p.getUuid())-now); p.sendMessage(Text.literal("Cooldown: " + (cd/20f) + "s").formatted(Formatting.RED), true); return; }
        BreathingStyle style = getStyle(p);
        int form = getForm(p);
        World w = p.getWorld();
        boolean isThunderGodspeed = style==BreathingStyle.THUNDER && (form==0||form==1||form==2);
        boolean isWaterCalm = style==BreathingStyle.WATER && form==10;
        boolean isGodUlt = style==BreathingStyle.GOD;

        w.playSound(null, p.getBlockPos(), style.getSound(), SoundCategory.PLAYERS, 1.5f, 0.9f + w.random.nextFloat()*0.4f);
        // Auto-cycle to next form after use for combo feel
        // Apply effects per breathing
        applyStyleEffect(p, style, form, w);
        COOLDOWN.put(p.getUuid(), now + (isGodUlt ? 20 : isWaterCalm? 60 : isThunderGodspeed? 30 : 40));
        // Advance form index automatically for anime combo
        PLAYER_FORM.put(p.getUuid(), (form+1)% style.getForms().length);
    }

    private static void applyStyleEffect(PlayerEntity p, BreathingStyle s, int form, World w){
        Vec3d look = p.getRotationVec(1f);
        double range = s==BreathingStyle.GOD ? 18 : 7;
        double dmg = s==BreathingStyle.GOD ? 100 : s==BreathingStyle.SUN || s==BreathingStyle.MOON ? 22 : 16;
        if (s==BreathingStyle.THUNDER) { // dash
            p.setVelocity(look.multiply(2.5 + form*0.4));
            p.velocityModified = true; w.playSound(null,p.getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS,1,2);
            dmg += 6;
        }
        if (s==BreathingStyle.WIND) { p.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 2)); range=9; }
        if (s==BreathingStyle.FLAME) dmg+=4;
        if (s==BreathingStyle.WATER && form==10){ // Dead Calm - bubble shield
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 60, 4)); range=5;
        }

        // Particle explosion shape per style
        spawnFormParticles(p, s, form);
        // Damage nearby entities
        Box box = new Box(p.getPos().add(-range,-range,-range), p.getPos().add(range,range,range));
        for (LivingEntity e : w.getEntitiesByClass(LivingEntity.class, box, en -> en != p && en.isAlive())){
            double dist = p.distanceTo(e);
            if (dist > range) continue;
            float fDmg = (float)(dmg * (1 - dist/(range*1.4)));
            if (isGodMode(p)) fDmg *= 3;
            e.damage(w.getDamageSources().playerAttack((ServerPlayerEntity)p), fDmg);
            e.setVelocity(look.multiply(0.6));
            e.velocityModified = true;
            e.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 1));
            if (s==BreathingStyle.FLAME) e.setOnFireFor(4);
            if (s==BreathingStyle.THUNDER) w.playSound(null, e.getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS,0.5f,1.5f);
        }
        // God mode ultimate screen shake via particles
        if (s==BreathingStyle.GOD){
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 60, 2));
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, 2));
            if (w instanceof net.minecraft.server.world.ServerWorld sw){
                sw.spawnParticles(ParticleTypes.EXPLOSION, p.getX(), p.getY()+1, p.getZ(), 3, 1,1,1,0);
                sw.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, p.getX(), p.getY()+1, p.getZ(), 60, range/2, 1, range/2, 0.2);
            }
        }
    }

    private static void spawnSwitchParticles(PlayerEntity p){
        if (!(p.getWorld() instanceof net.minecraft.server.world.ServerWorld sw)) return;
        BreathingStyle s = getStyle(p);
        sw.spawnParticles(s.getParticle(), p.getX(), p.getY()+1.2, p.getZ(), 25, 0.6,0.5,0.6, 0.15);
        sw.spawnParticles(ParticleTypes.END_ROD, p.getX(), p.getY()+1.2, p.getZ(), 12, 0.4,0.4,0.4,0.05);
    }
    private static void spawnFormParticles(PlayerEntity p, BreathingStyle s, int form){
        if (!(p.getWorld() instanceof net.minecraft.server.world.ServerWorld sw)) return;
        Vec3d look = p.getRotationVec(1f);
        // Ring + slash
        for(int i=0;i<36;i++){
            double a = Math.toRadians(i*10);
            double r = 1.8 + form*0.15;
            double x = p.getX() + Math.cos(a)*r;
            double z = p.getZ() + Math.sin(a)*r;
            sw.spawnParticles(s.getParticle(), x, p.getY()+1, z, 1, 0,0.05,0,0.02);
        }
        // Forward slash trail
        for(int i=0;i<14;i++){
            double t = i*0.6;
            sw.spawnParticles(s.getParticle(), p.getX()+look.x*t, p.getY()+1+Math.sin(i*0.7)*0.5, p.getZ()+look.z*t, 2, 0.15,0.15,0.15,0.04);
            if (s==BreathingStyle.THUNDER) sw.spawnParticles(ParticleTypes.ELECTRIC_SPARK, p.getX()+look.x*t, p.getY()+1, p.getZ()+look.z*t, 3,0.2,0.2,0.2,0.1);
            if (s==BreathingStyle.FLAME) sw.spawnParticles(ParticleTypes.FLAME, p.getX()+look.x*t, p.getY()+1, p.getZ()+look.z*t, 3,0.2,0.2,0.2,0.08);
            if (s==BreathingStyle.WATER) sw.spawnParticles(ParticleTypes.SPLASH, p.getX()+look.x*t, p.getY()+1, p.getZ()+look.z*t, 5,0.3,0.2,0.3,0.2);
            if (s==BreathingStyle.WIND) sw.spawnParticles(ParticleTypes.CLOUD, p.getX()+look.x*t, p.getY()+1, p.getZ()+look.z*t, 2,0.3,0.1,0.3,0.05);
            if (s==BreathingStyle.MIST) sw.spawnParticles(ParticleTypes.CLOUD, p.getX()+look.x*t, p.getY()+1.5, p.getZ()+look.z*t, 4,0.5,0.3,0.5,0.02);
        }
        // Dragon effect for big forms
        if (form >= s.getForms().length-2 || s==BreathingStyle.GOD){
            for(int i=0;i<20;i++){
                double h = i*0.35;
                double ang = i*0.8;
                sw.spawnParticles(ParticleTypes.DRAGON_BREATH, p.getX()+Math.cos(ang)*1.2, p.getY()+h, p.getZ()+Math.sin(ang)*1.2, 1,0,0,0,0);
            }
        }
    }
}
