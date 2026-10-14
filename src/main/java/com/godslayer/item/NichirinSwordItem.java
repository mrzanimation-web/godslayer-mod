package com.godslayer.item;

import com.godslayer.breathing.BreathingManager;
import com.godslayer.breathing.BreathingStyle;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import java.util.List;

public class NichirinSwordItem extends Item {
    private final BreathingStyle style;
    public NichirinSwordItem(BreathingStyle style, RegistryKey<Item> key){
        super(new Settings().registryKey(key).maxCount(1).fireproof()
            .attributeModifiers(AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Identifier.of("godslayer","nichirin_"+style.name().toLowerCase()), style==BreathingStyle.GOD? 28 : style==BreathingStyle.SUN? 16 : 13, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Identifier.of("godslayer","nichirin_speed"), 1.6, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build())
        );
        this.style = style;
    }
    @Override public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        if (!world.isClient) BreathingManager.triggerForm(user);
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext ctx, List<Text> tooltip, TooltipType type){
        tooltip.add(Text.literal("☾ Nichirin Blade • " + style.getDisplayName() + " (" + style.getJapanese() + ")").formatted(style.getColor(), Formatting.BOLD));
        tooltip.add(Text.literal("Forms: " + style.getForms().length + " • Right-Click: Execute Form").formatted(Formatting.GRAY));
        for(int i=0;i<Math.min(4, style.getForms().length);i++) tooltip.add(Text.literal("  " + (i+1)+". " + style.getForms()[i]).formatted(Formatting.DARK_GRAY));
        if (style.getForms().length>4) tooltip.add(Text.literal("  ... +" + (style.getForms().length-4) + " more (Press V to cycle)").formatted(Formatting.DARK_GRAY));
        tooltip.add(Text.literal("R/SHIFT+R: Switch Breathing • V: Next Form • G: God Mode").formatted(Formatting.YELLOW));
        tooltip.add(Text.literal("Hashira Grade • Unbreakable • 13 dmg").formatted(Formatting.LIGHT_PURPLE));
    }
    public BreathingStyle getStyle(){ return style; }
}
