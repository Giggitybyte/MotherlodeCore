package motherlode.spelunky.item;

import motherlode.spelunky.MotherlodeSpelunkyEntities;
import motherlode.spelunky.entity.ExplosiveItemEntity;
import net.minecraft.entity.EntityType;

public class DynamiteItem extends ExplosiveItem {
    public DynamiteItem(Settings settings) {
        super(MotherlodeSpelunkyEntities.DYNAMITE_ENTITY, settings);
    }
    
    protected DynamiteItem(EntityType<ExplosiveItemEntity> entityType, Settings settings) {
        super(entityType, settings);
    }
}
