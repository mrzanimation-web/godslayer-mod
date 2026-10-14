package com.godslayer.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import java.util.List;

public class GodSwordItem extends Item {
    private final String godName;
    private final int godDamage;
    private final int hexColor;

    public GodSwordItem(Settings settings, String godName, int godDamage, int hexColor){
        super(settings.attributeModifiers(AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Identifier.of("godslayer","god_damage_"+godName.hashCode()), godDamage, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Identifier.of("godslayer","god_speed_"+godName.hashCode()), 3.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.of("godslayer","god_range_"+godName.hashCode()), 2.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build()));
        this.godName = godName; this.godDamage = godDamage; this.hexColor = hexColor;
    }
    @Override public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        if (!world.isClient){
            user.heal(8f);
            user.getHungerManager().add(6, 1f);
            user.sendMessage(Text.literal("✦ " + godName + " PULSE ✦").formatted(Formatting.GOLD, Formatting.BOLD), true);
            world.playSound(null, user.getBlockPos(), net.minecraft.sound.SoundEvents.ENTITY_WARDEN_SONIC_BOOM, net.minecraft.sound.SoundCategory.PLAYERS, 1f, 1.4f);
            if (world instanceof net.minecraft.server.world.ServerWorld sw){
                sw.spawnParticles(net.minecraft.particle.ParticleTypes.SOUL_FIRE_FLAME, user.getX(), user.getY()+1, user.getZ(), 30, 0.7,0.5,0.7, 0.12);
                sw.spawnParticles(net.minecraft.particle.ParticleTypes.END_ROD, user.getX(), user.getY()+1, user.getZ(), 20, 0.5,0.5,0.5, 0.08);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext ctx, List<Text> tooltip, net.minecraft.item.tooltip.TooltipType type){
        tooltip.add(Text.literal("⚔ " + godName).formatted(Formatting.GOLD, Formatting.BOLD));
        tooltip.add(Text.literal("Damage: " + godDamage + " ♥ One-Shot Most Mobs").formatted(Formatting.RED));
        tooltip.add(Text.literal("Speed: GODLIKE | Reach: +2 blocks").formatted(Formatting.AQUA));
        tooltip.add(Text.literal("Right-Click: God Pulse Heal + Blast").formatted(Formatting.YELLOW));
        tooltip.add(Text.literal("Unbreakable • Fireproof • Ultra Enchanted").formatted(Formatting.LIGHT_PURPLE));
    }
}
