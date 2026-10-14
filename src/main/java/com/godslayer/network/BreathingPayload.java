package com.godslayer.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record BreathingPayload(String action, boolean shift) implements CustomPayload {
    public static final Identifier PACKET_ID = Identifier.of("godslayer", "breathing");
    public static final CustomPayload.Id<BreathingPayload> ID = new CustomPayload.Id<>(PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, BreathingPayload> CODEC = PacketCodec.of(
        (value, buf) -> { buf.writeString(value.action); buf.writeBoolean(value.shift); },
        buf -> new BreathingPayload(buf.readString(), buf.readBoolean())
    );
    @Override public Id<? extends CustomPayload> getId() { return ID; }
}
