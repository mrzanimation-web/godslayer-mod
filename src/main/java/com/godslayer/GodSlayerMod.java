package com.godslayer;

import com.godslayer.registry.ModItemGroups;
import com.godslayer.registry.ModItems;
import com.godslayer.registry.ModParticles;
import com.godslayer.registry.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import com.godslayer.breathing.BreathingManager;
import com.godslayer.breathing.BreathingStyle;
import com.godslayer.network.BreathingPayload;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GodSlayerMod implements ModInitializer {
    public static final String MOD_ID = "godslayer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("⚔️ GOD SLAYER INITIALIZING - DEMON SLAYER x GOD TOOLS ⚔️");

        ModItems.register();
        ModItemGroups.register();
        ModParticles.register();
        ModSounds.register();

        // Networking for breathing forms
        PayloadTypeRegistry.playC2S().register(BreathingPayload.ID, BreathingPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(BreathingPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> {
                if (payload.action().equals("form")) {
                    BreathingManager.triggerForm(player);
                } else if (payload.action().equals("switch")) {
                    BreathingManager.cycleBreathing(player, payload.shift());
                } else if (payload.action().equals("godmode")) {
                    BreathingManager.toggleGodMode(player);
                }
            });
        });

        // Commands: /godslayer give <player> <item> | /breathing set <style>
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) -> {
            dispatcher.register(CommandManager.literal("godslayer")
                .then(CommandManager.literal("givegodset").executes(ctx -> {
                    var p = ctx.getSource().getPlayerOrThrow();
                    ModItems.giveGodSet(p);
                    ctx.getSource().sendFeedback(() -> Text.literal("✨ Granted GOD SET! You are now UNSTOPPABLE ✨").formatted(Formatting.GOLD, Formatting.BOLD), false);
                    return 1;
                }))
                .then(CommandManager.literal("breathing").then(CommandManager.literal("list").executes(ctx -> {
                    String list = String.join(", ", java.util.Arrays.stream(BreathingStyle.values()).map(BreathingStyle::getDisplayName).toList());
                    ctx.getSource().sendFeedback(() -> Text.literal("Breathings: " + list).formatted(Formatting.AQUA), false);
                    return 1;
                })))
            );
            dispatcher.register(CommandManager.literal("nichirin").executes(ctx -> {
                var p = ctx.getSource().getPlayerOrThrow();
                ModItems.giveAllNichirin(p);
                ctx.getSource().sendFeedback(() -> Text.literal("🌸 Granted ALL 14 Nichirin Swords! Press R to switch Breathing!").formatted(Formatting.LIGHT_PURPLE), false);
                return 1;
            }));
        });

        LOGGER.info("✅ GOD SLAYER READY! /godslayer givegodset | /nichirin | Keys: R=Switch V=Form G=GodMode");
    }
}
