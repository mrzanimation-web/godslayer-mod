package com.godslayer.item;

import net.minecraft.block.BlockState;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import java.util.List;

public class GodToolItem extends Item {
    public enum ToolType { PICKAXE, AXE, SHOVEL, HOE }
    private final ToolType toolType;
    private final String name;
    public GodToolItem(Settings s, ToolType t, String name, float miningSpeed){
        super(s
          .fireproof()
          .attributeModifiers(AttributeModifiersComponent.builder()
            .add(EntityAttributes.BLOCK_BREAK_SPEED, new EntityAttributeModifier(Identifier.of("godslayer","god_mine_"+t.name()), miningSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
            .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Identifier.of("godslayer","god_tool_dmg"), 18, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
            .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Identifier.of("godslayer","god_tool_speed"), 2.5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
            .build())
        );
        this.toolType = t; this.name = name;
    }
    @Override public float getMiningSpeed(ItemStack stack, BlockState state){ return 120f; } // instant break
    @Override public boolean canMine(BlockState state, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, PlayerEntity miner){ return true; }
    @Override public boolean isCorrectForDrops(ItemStack stack, BlockState state){ return true; }
    @Override public void appendTooltip(ItemStack stack, TooltipContext ctx, List<Text> tooltip, TooltipType type){
        tooltip.add(Text.literal("⛏ " + name).formatted(Formatting.GOLD, Formatting.BOLD));
        tooltip.add(Text.literal("INSTANT MINE • Silk Touch + Fortune X").formatted(Formatting.AQUA));
        tooltip.add(Text.literal("3x3 Vein Miner: Sneak+Break").formatted(Formatting.YELLOW));
        tooltip.add(Text.literal("Unbreakable • Tool: " + toolType).formatted(Formatting.GRAY));
    }
}
