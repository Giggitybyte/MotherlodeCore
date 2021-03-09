package motherlode.spelunky;

import motherlode.spelunky.entity.ExplosiveItemEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class MotherlodeSpelunkyEntities {
    public static final EntityType<ExplosiveItemEntity> BOMB_ENTITY = registerExplosive("bomb", EntityDimensions.fixed(0.25f, 0.25f));
    public static final EntityType<ExplosiveItemEntity> DYNAMITE_ENTITY = registerExplosive("dynamite", EntityDimensions.fixed(0.25f, 0.25f));
    public static final EntityType<ExplosiveItemEntity> STICKY_DYNAMITE_ENTITY = registerExplosive("dynamite_sticky", EntityDimensions.fixed(0.25f, 0.25f));
    
    public static void init() {
    }
    
    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> entity) {
        return Registry.register(Registry.ENTITY_TYPE, MotherlodeModule.id(name), entity);
    }
    
    private static EntityType<ExplosiveItemEntity> registerExplosive(String name, EntityDimensions size) {
        return register(name, FabricEntityTypeBuilder.<ExplosiveItemEntity>create(SpawnGroup.MISC, ExplosiveItemEntity::new)
                .dimensions(size)
                .trackRangeBlocks(4)
                .trackedUpdateRate(10)
                .build()
        );
    }
}
