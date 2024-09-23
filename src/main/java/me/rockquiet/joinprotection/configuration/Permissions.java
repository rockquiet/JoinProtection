package me.rockquiet.joinprotection.configuration;

public final class Permissions {

    public static final String JOIN_PROTECTION = "joinprotection";
    public static final String USE = JOIN_PROTECTION + ".use";
    public static final String USE_WORLD = JOIN_PROTECTION + ".use-world";
    public static final String PROTECT = JOIN_PROTECTION + ".protect";
    public static final String CANCEL = JOIN_PROTECTION + ".cancel";
    public static final String RELOAD = JOIN_PROTECTION + ".reload";
    public static final String BYPASS_CANCEL_ON_MOVE = JOIN_PROTECTION + ".bypass.cancel-on-move";
    public static final String BYPASS_CANCEL_ON_ATTACK = JOIN_PROTECTION + ".bypass.cancel-on-attack";
    public static final String BYPASS_CANCEL_ON_BLOCK_INTERACT = JOIN_PROTECTION + ".bypass.cancel-on-block-interact";
    public static final String BYPASS_WORLD_LIST = JOIN_PROTECTION + ".bypass.world-list";

    private Permissions() {
    }
}
