package motherlode.spelunky.item;

import motherlode.spelunky.MotherlodeModule;
import motherlode.spelunky.entity.ExplosiveItemEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public abstract class ExplosiveItem extends Item {
    private final EntityType<? extends ExplosiveItemEntity> entityType;
    
    public ExplosiveItem(EntityType<? extends ExplosiveItemEntity> entityType, Settings settings) {
        super(settings);
        this.entityType = entityType;
    }
    
    public Identifier getTexturePath() {
        String itemName = Registry.ITEM.getId(this).getPath();
        return MotherlodeModule.id("textures/item/" + itemName + ".png");
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 0.5f, 0.6f);
        // player.getItemCooldownManager().set(this, 60);
        
        ItemStack itemStack = player.getStackInHand(hand);
        
        if (!world.isClient) {
            ExplosiveItemEntity explosiveEntity = new ExplosiveItemEntity(entityType, world);
            Vec3d eyePos = player.getEyePos();
            
            explosiveEntity.updateTrackedPosition(eyePos.x, eyePos.y, eyePos.z);
            
            explosiveEntity.setPos(eyePos.x, eyePos.y, eyePos.z);
            explosiveEntity.setProperties(player, player.pitch, player.yaw, 0.0f, 0.8f, 0.2f);
            explosiveEntity.setOwner(player);
            
            world.spawnEntity(explosiveEntity);
        }
        
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!player.getAbilities().creativeMode) itemStack.decrement(1);
        
        return TypedActionResult.success(itemStack, world.isClient());
    }
    
    
}
