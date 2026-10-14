package com.godslayer.client;

import com.godslayer.breathing.BreathingManager;
import com.godslayer.breathing.BreathingStyle;
import com.godslayer.network.BreathingPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class GodSlayerClient implements ClientModInitializer {
    private static KeyBinding KB_BREATHING;
    private static KeyBinding KB_FORM;
    private static KeyBinding KB_GODMODE;

    @Override public void onInitializeClient() {
        KB_BREATHING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.godslayer.switch_breathing", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "category.godslayer"));
        KB_FORM = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.godslayer.next_form", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "category.godslayer"));
        KB_GODMODE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.godslayer.godmode", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "category.godslayer"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            boolean shift = InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT);
            while (KB_BREATHING.wasPressed()){
                ClientPlayNetworking.send(new BreathingPayload("switch", shift));
                // client feedback
                if (shift) BreathingManager.cycleBreathing(client.player, true); else BreathingManager.cycleBreathing(client.player, false);
            }
            while (KB_FORM.wasPressed()){
                boolean isFormKey = true;
                // if shift held, go reverse
                if (shift) BreathingManager.cycleForm(client.player, true); else BreathingManager.cycleForm(client.player, false);
                // also trigger form on V? No, V cycles, right click triggers. Keep cycling only.
            }
            while (KB_GODMODE.wasPressed()){
                ClientPlayNetworking.send(new BreathingPayload("godmode", false));
                BreathingManager.toggleGodMode(client.player);
            }
            // Right-click form trigger is via item use -> server payload "form" sent from server side? For now also allow KEY_F to trigger form
            if (InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_F) && client.player.age % 4==0){
                // throttle: send only when pressed with debounce via item use already; this is extra hotkey
            }
        });

        // HUGE anime HUD
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.options.hudHidden) return;
            BreathingStyle s = BreathingManager.getStyle(mc.player);
            int form = BreathingManager.getForm(mc.player);
            boolean god = BreathingManager.isGodMode(mc.player);
            int w = mc.getWindow().getScaledWidth();
            int h = mc.getWindow().getScaledHeight();

            // Top center banner
            String title = "〖 " + s.getDisplayName().toUpperCase() + " 〗  " + s.getJapanese();
            String formName = "Form " + (form+1) + " — " + s.getFormName(form);
            int color = s.getHex() | 0xFF000000;
            int titleW = mc.textRenderer.getWidth(title);
            // background bar
            drawContext.fill(w/2 - titleW/2 - 10, 6, w/2 + titleW/2 + 10, 28, 0x80000000);
            drawContext.drawTextWithShadow(mc.textRenderer, Text.literal(title).formatted(s.getColor(), Formatting.BOLD), w/2 - titleW/2, 10, color);
            int fw = mc.textRenderer.getWidth(formName);
            drawContext.drawTextWithShadow(mc.textRenderer, formName, w/2 - fw/2, 30, 0xFFFFFF);

            // God mode indicator
            if (god){
                String godT = "★ GOD MODE ACTIVE ★";
                int gw = mc.textRenderer.getWidth(godT);
                drawContext.fill(w/2 - gw/2 -6, h - 50, w/2 + gw/2 +6, h -36, 0xAAFFD700);
                drawContext.drawTextWithShadow(mc.textRenderer, godT, w/2 - gw/2, h -46, 0xFF000000);
            }
            // Key hints bottom left
            drawContext.drawTextWithShadow(mc.textRenderer, Text.literal("[R] Switch  [V] Form  [RightClick] Execute  [G] GodMode").formatted(Formatting.GRAY), 6, h-14, 0xFFFFFF);
            // Health-style breathing gauge right side
            int barW = 80; int barH = 8;
            drawContext.fill(w - barW - 10, 10, w -10, 10+barH, 0xFF333333);
            float prog = (form+1)/(float)s.getForms().length;
            drawContext.fill(w - barW -10, 10, w -10 - (int)((1-prog)*barW), 10+barH, color);
            drawContext.drawTextWithShadow(mc.textRenderer, (form+1)+"/"+s.getForms().length, w - barW -10, 20, 0xFFFFFF);
        });
    }
}
