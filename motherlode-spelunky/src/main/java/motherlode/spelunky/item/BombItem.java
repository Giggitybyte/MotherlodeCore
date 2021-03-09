package motherlode.spelunky.item;

import motherlode.spelunky.MotherlodeSpelunkyEntities;

public class BombItem extends ExplosiveItem {
    public BombItem(Settings settings) {
        super(MotherlodeSpelunkyEntities.BOMB_ENTITY, settings);
    }
}
