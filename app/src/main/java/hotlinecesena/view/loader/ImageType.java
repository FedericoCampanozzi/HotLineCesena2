package hotlinecesena.view.loader;

/**
 * Collections of all the possible images that can be displayed on the screen
 */
public enum ImageType {

    PLAYER("Player.png"),

    PLAYER_PISTOL("PlayerPistol.png"),

    PLAYER_RIFLE("PlayerRifle.png"),

    ENEMY_IDLE("EnemyIdle.png"),

    ENEMY_RANDOM_MOVEMENTS("EnemyRandomMovement.png"),

    ENEMY_PATROLING("EnemyIdle.png"),

    BULLET("shoot.png"),

    AMMO_PISTOL("AmmoPistol.png"),

    AMMO_RIFLE("AmmoRifle.png"),

    PISTOL("Pistol.png"),

    RIFLE("Rifle.png"),

    MEDKIT("medkit.png"),

    ICON("icon.png"),

    TITLE("Title.png"),

    WALL("wall.png"),

    FLOOR("floor.png"),

    BOX("box.png"),

    GRASS("grass.png"),

    TABLE("table.png"),

    CHAIR("chair.png"),

    KITCHEN("kitchen.png"),

    OVEN("oven.png");

    private final String path;

    /**
     * @param path the relative path to each image file
     * @see String 
     */
    private ImageType(final String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return this.path;
    }
}
