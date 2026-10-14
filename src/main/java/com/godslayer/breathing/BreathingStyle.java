package com.godslayer.breathing;

import net.minecraft.util.Formatting;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public enum BreathingStyle {
    WATER("Water", "Mizu no Kokyu", Formatting.BLUE, 0x1E90FF, ParticleTypes.DRIPPING_WATER, SoundEvents.ENTITY_DOLPHIN_SPLASH,
        new String[]{"1st Form: Water Surface Slash","2nd: Water Wheel","3rd: Dance of Rapid Current","4th: Striking Tide","5th: Blessed Rain","6th: Whirlpool","7th: Drop Ripple Thrust","8th: Waterfall Basin","9th: Splashing Water Flow","10th: Constant Flux","11th: Dead Calm (Giyu)"}),
    FLAME("Flame", "Hi no Kokyu", Formatting.RED, 0xFF4500, ParticleTypes.FLAME, SoundEvents.ENTITY_BLAZE_SHOOT,
        new String[]{"1st: Unknowing Fire","2nd: Rising Scorching Sun","3rd: Blazing Universe","4th: Fake Rainbow","5th: Flame Tiger","6th: ???","9th: Rengoku (Secret)"}),
    THUNDER("Thunder", "Kaminari no Kokyu", Formatting.YELLOW, 0xFFD700, ParticleTypes.ELECTRIC_SPARK, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
        new String[]{"1st: Thunderclap & Flash (6-fold)","1st: 8-fold","1st: Godspeed","2nd: Rice Spirit","3rd: Thunder Swarm","6th: Rumble & Flash"}),
    WIND("Wind", "Kaze no Kokyu", Formatting.GREEN, 0x90EE90, ParticleTypes.CLOUD, SoundEvents.ENTITY_PHANTOM_FLAP,
        new String[]{"1st: Dust Whirlwind Cutter","2nd: Claws-Purifying Wind","3rd: Clean Storm Wind Tree","4th: Rising Dust Storm","7th: Gale Sudden Gusts"}),
    STONE("Stone", "Iwa no Kokyu", Formatting.GRAY, 0x808080, ParticleTypes.CRIT, SoundEvents.ENTITY_IRON_GOLEM_ATTACK,
        new String[]{"1st: Serpentinite Bipolar","2nd: Upper Smash","3rd: Stone Skin","4th: Volcanic Rock Rapid Conquest","5th: Arcs of Justice"}),
    MIST("Mist", "Kasumi no Kokyu", Formatting.AQUA, 0xADD8E6, ParticleTypes.CLOUD, SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL,
        new String[]{"1st: Low Clouds Distant Haze","2nd: Eight-Layered Mist","5th: Sea of Clouds & Haze","6th: Lunar Dispersing Mist","7th: Obscuring Clouds"}),
    LOVE("Love", "Koi no Kokyu", Formatting.LIGHT_PURPLE, 0xFF69B4, ParticleTypes.HEART, SoundEvents.ENTITY_CAT_PURR,
        new String[]{"1st: Shivers of First Love","2nd: Love Pangs","3rd: Cat-Legged Wind of Love","5th: Swaying Love Wildclaw","6th: Cat-Legged Wind"}),
    SERPENT("Serpent", "Hebi no Kokyu", Formatting.DARK_PURPLE, 0x9370DB, ParticleTypes.WITCH, SoundEvents.ENTITY_SPIDER_AMBIENT,
        new String[]{"1st: Winding Serpent Slash","2nd: Venom Fangs of Narrow Head","3rd: Coil Choke","4th: Twin-Headed Reptile","5th: Slithering Serpent"}),
    SOUND("Sound", "Oto no Kokyu", Formatting.GOLD, 0xFFA500, ParticleTypes.NOTE, SoundEvents.BLOCK_NOTE_BLOCK_BELL,
        new String[]{"1st: Roar","4th: Constant Resounding Slashes","5th: String Performance"}),
    INSECT("Insect", "Mushi no Kokyu", Formatting.DARK_AQUA, 0x40E0D0, ParticleTypes.SPORE_BLOSSOM_AIR, SoundEvents.ENTITY_BEE_LOOP,
        new String[]{"Butterfly Dance: Caprice","Dance of Bee Sting","Dance of Dragonfly","Dance of Centipede"}),
    SUN("Sun", "Hi no Kokyu / Hinokami Kagura", Formatting.GOLD, 0xFF8C00, ParticleTypes.FLAME, SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
        new String[]{"Dance","Clear Blue Sky","Raging Sun","Fake Rainbow","Fire Wheel","Burning Bones Summer Sun","Beneficial Breeze","Sunflower Thrust","Setting Sun Transformation","Parhelion Rainbow","Dragon Sun Halo Head Dance","Flame Dance (13 Forms) - TANJIRO"}),
    MOON("Moon", "Tsuki no Kokyu", Formatting.DARK_GRAY, 0xC0C0C0, ParticleTypes.SOUL, SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
        new String[]{"1st: Dark Moon Evening Palace","2nd: Pearl Flower Moongazing","5th: Moon Spirit Calamitous Eddy","16th: Moonbow Half Moon (KOKUSHIBO)"}),
    BEAST("Beast", "Kedamono no Kokyu", Formatting.DARK_GREEN, 0x228B22, ParticleTypes.CRIT, SoundEvents.ENTITY_WOLF_GROWL,
        new String[]{"1st Fang: Pierce","2nd: Slice","3rd: Devour","7th: Spatial Awareness","10th: Whirling Fangs (INOSUKE)"}),
    GOD("GOD BREATH", "Kami no Kokyu - OMNIBREATH", Formatting.GOLD, 0xFFFFFF, ParticleTypes.SOUL_FIRE_FLAME, SoundEvents.ENTITY_WITHER_SPAWN,
        new String[]{"OMNI FORM 1: Divine Water Dragon","OMNI 2: Hellfire Annihilation","OMNI 3: Godspeed Thunder God","OMNI 4: Cosmic Wind Storm","OMNI 5: Big Bang Slash - ONE SHOT!","ULTIMATE: TRANSCENDENT GOD RAINBOW"});

    private final String displayName;
    private final String japanese;
    private final Formatting color;
    private final int hexColor;
    private final ParticleEffect particle;
    private final SoundEvent sound;
    private final String[] forms;

    BreathingStyle(String displayName, String jp, Formatting color, int hex, ParticleEffect particle, SoundEvent sound, String[] forms) {
        this.displayName = displayName; this.japanese = jp; this.color = color; this.hexColor = hex; this.particle = particle; this.sound = sound; this.forms = forms;
    }
    public String getDisplayName(){ return displayName; }
    public String getJapanese(){ return japanese; }
    public Formatting getColor(){ return color; }
    public int getHex(){ return hexColor; }
    public ParticleEffect getParticle(){ return particle; }
    public SoundEvent getSound(){ return sound; }
    public String[] getForms(){ return forms; }
    public String getFormName(int idx){ return forms[Math.floorMod(idx, forms.length)]; }
}
