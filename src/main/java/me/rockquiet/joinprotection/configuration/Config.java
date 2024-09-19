package me.rockquiet.joinprotection.configuration;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import me.rockquiet.joinprotection.protection.ProtectionType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Particle;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("FieldMayBeFinal")
@Configuration
public final class Config {

    public static final String HEADER = "JoinProtection by rockquiet";

    public PluginSection plugin = new PluginSection();
    public DisplaySection display = new DisplaySection();
    @Comment("cancel active protection")
    public CancelSection cancel = new CancelSection();
    @Comment("when the player is...")
    public ModulesSection modules = new ModulesSection();
    @Comment("particle aura around player with active protection")
    public ParticlesSection particles = new ParticlesSection();
    @Comment("plays a sound to players attacking others with active protection")
    public SoundSection sound = new SoundSection();
    @Comment("use \"\" to disable a message")
    public MessagesSection messages = new MessagesSection();
    @Comment("Toggle/Configure integrations into other plugins here.")
    public IntegrationSection integration = new IntegrationSection();

    @Comment("do not edit/remove this")
    public int fileVersion = 2;

    @Configuration
    public static class PluginSection {

        @Comment("if the plugin should search for updates on server start")
        public boolean updateChecks = true;

        @Comment("""
                a list of worlds where the plugin should or should not work
                DISABLED: world-list is not used
                BLACKLIST: the plugin will not work in worlds listed below
                WHITELIST: the plugin will only work in worlds listed below""")
        public String listType = "disabled";
        public List<String> worldList = List.of("ExampleEntryForWorldList", "AnotherExampleWorld");

        @Comment("""
                join protection time in seconds
                add NUMBER amount of seconds to a player: joinprotection.plus-NUMBER""")
        public int protectionTime = 10;

        @Comment("""
                also protect players that switch worlds (i.e. go through a nether portal)
                set this to 0 to disable""")
        public int worldChangeProtectionTime = 10;

        @Comment("only give join protection to players who are joining the server for the first time.")
        public boolean firstJoinOnly = false;
    }

    @Configuration
    public static class DisplaySection {

        @Comment("""
                where to display the remaining time, etc.
                allowed: ACTION_BAR, TITLE, SUBTITLE, CHAT""")
        public String location = "ACTION_BAR";

        @Comment("title display time in ticks")
        public TitleSection title = new TitleSection();

        @Configuration
        public static class TitleSection {
            public int fadeIn = 0;
            public int stay = 20;
            public int fadeOut = 20;

            public Title.Times toTimes() {
                return Title.Times.times(
                        Ticks.duration(fadeIn),
                        Ticks.duration(stay),
                        Ticks.duration(fadeOut)
                );
            }
        }
    }

    @Configuration
    public static class CancelSection {

        @Comment("if attacking an entity")
        public boolean onAttack = true;

        @Comment("if player moved (or changes their world)")
        public OnMoveSection onMove = new OnMoveSection();

        @Comment("if player breaks/places blocks")
        public boolean onBlockInteract = false;

        @Configuration
        public static class OnMoveSection {
            public boolean enabled = true;
            @Comment("if the y-axis should be ignored")
            public boolean ignoreYAxis = true;
            @Comment("distance the player is allowed to move without cancelling")
            public double distance = 3.0;
        }
    }

    @Configuration
    public static class ModulesSection {

        @Comment("...damaged by an entity")
        public boolean disableDamageByEntities = true;

        @Comment("...damaged by a block")
        public boolean disableDamageByBlocks = false;

        @Comment("...damaged by whatever not cancelled by the first two (fall damage, etc.)")
        public boolean disableDamage = false;

        @Comment("...targeted by an entity")
        public DisableEntityTargetingSection disableEntityTargeting = new DisableEntityTargetingSection();

        @Comment("do not allow the player to drop items")
        public boolean disableItemDrops = false;

        @Comment("do not allow the player to pickup items")
        public boolean disableItemPickup = false;

        @Configuration
        public static class DisableEntityTargetingSection {
            public boolean enabled = true;
            @Comment("this is the range used to clear entity targets on world change")
            public double horizontalRange = 16.0;
            public double verticalRange = 16.0;
        }
    }

    @Configuration
    public static class ParticlesSection {
        public boolean enabled = true;

        @Comment("""
                if the server MSPT take longer than this value, particles will no longer be displayed
                INFO: this is not supported on spigot servers""")
        public double maximumMspt = 50.0;

        @Comment("""
                all particles: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html
                good-looking particles / display duration:
                mid: WAX_ON, WAX_OFF, ENCHANTMENT_TABLE
                long: VILLAGER_HAPPY""")
        public String type = "ENCHANTMENT_TABLE";

        @Comment("the number of particles which get used")
        public int amount = 1;

        @Comment("how often the particles refresh (in ticks)")
        public int refreshRate = 4;

        @Comment("particle aura size")
        public int circles = 4;

        public Particle toParticle() {
            return Particle.valueOf(type);
        }
    }

    @Configuration
    public static class SoundSection {
        public boolean enabled = true;

        @Comment("all sounds: https://docs.andre601.ch/Spigot-Sounds/sounds")
        public String type = "ITEM_SHIELD_BLOCK";

        @Comment("the volume of the sound")
        public double volume = 0.8;

        @Comment("the pitch of the sound")
        public double pitch = 1.0;

        public Sound toSound() {
            return Sound.sound(Key.key(type.toLowerCase(Locale.ROOT)), Sound.Source.PLAYER, (float) volume, (float) pitch);
        }
    }

    @Configuration
    public static class MessagesSection {

        public String prefix = "<gray>[</gray><gradient:#7287fd:#04a5e5>JoinProtection</gradient><gray>]</gray>";
        public String noPerms = "<prefix> <red>No permissions!";
        public String commandUsage = "<prefix> <red>Usage: /<command_usage>";
        public String playerNotFound = "<prefix> <red>Target player <dark_red><player> <red>not found!";
        public String invalidNumberFormat = "<prefix> <red>The specified number <dark_red><number> <red>is invalid!";
        public String numberMustBePositive = "<prefix> <red>The specified number <dark_red><number> <red>must be positive!";
        public String alreadyProtected = "<prefix> <dark_red><player> <red>is already protected!";
        public String protect = "<prefix> <yellow><player> <green>is now protected for <yellow><time><green>s.";
        public String reload = "<prefix> <green>Configuration reloaded successfully.";
        public String cannotHurt = "<prefix> <gray>You cannot hurt <white><player> <gray>while they have active protection.";
        @Comment("Used in protection info display")
        public TypeSection type = new TypeSection();
        public String timeRemaining = "<type> <dark_gray><b>»</b> <white><time><gray>s";
        public String protectionEnded = "<type> <dark_gray><b>»</b> <gradient:#e64553:#d20f39>Elapsed!</gradient>";
        public String protectionDeactivated = "<type> <dark_gray><b>»</b> <gradient:#e64553:#d20f39>Disabled!</gradient>";
        public String protectionDeactivatedAttack = "<type> <dark_gray><b>»</b> <gradient:#e64553:#d20f39>Disabled because of your attack!</gradient>";

        @Configuration
        public static class TypeSection {
            public String join = "<gradient:#7287fd:#04a5e5>Join Protection</gradient>";
            public String world = "<gradient:#2ab52a:#39d19b>World Protection</gradient>";
            public String command = "<gradient:#ea6ffd:#a775e0>Protection</gradient>";

            public String get(ProtectionType type) {
                return switch (type) {
                    case JOIN -> join;
                    case WORLD -> world;
                    case COMMAND -> command;
                };
            }
        }
    }

    @Configuration
    public static class IntegrationSection {

        @Comment("https://www.spigotmc.org/resources/placeholderapi.6245")
        public PlaceholderAPISection placeholderapi = new PlaceholderAPISection();
        @Comment("https://modrinth.com/plugin/miniplaceholders")
        public MiniPlaceholdersSection miniplaceholders = new MiniPlaceholdersSection();
        @Comment("https://luckperms.net")
        public LuckPermsSection luckperms = new LuckPermsSection();

        @Configuration
        public static class PlaceholderAPISection {
            public boolean enabled = true;

            @Comment("%joinprotection_status% - displays current protection status of the player")
            public StatusSection status = new StatusSection("", " &8| &7Protected");
        }

        @Configuration
        public static class MiniPlaceholdersSection {
            public boolean enabled = true;

            @Comment("<joinprotection_status> - displays current protection status of the player")
            public StatusSection status = new StatusSection("", " <dark_gray>| <gray>Protected");
        }

        public record StatusSection(String notProtected, String protectionActive) {
        }

        @Configuration
        public static class LuckPermsSection {
            public boolean enabled = true;
        }
    }
}
