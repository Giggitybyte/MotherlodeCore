package motherlode.spelunky.entity;

import io.netty.buffer.Unpooled;
import motherlode.spelunky.MotherlodeModule;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ExplosiveItemEntity extends ThrownEntity {
    protected static final TrackedData<Boolean> FUSE_LIT = DataTracker.registerData(ExplosiveItemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> BOUNCE_COUNT = DataTracker.registerData(ExplosiveItemEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> BOUNCE_LIMIT = DataTracker.registerData(ExplosiveItemEntity.class, TrackedDataHandlerRegistry.INTEGER);
    
    private final Identifier spawnPacketId;
    private final int bounceLimit;
    
    public ExplosiveItemEntity(EntityType<? extends ExplosiveItemEntity> entityType, World world, int bounceLimit) {
        super(entityType, world);
        
        String entityName = Registry.ENTITY_TYPE.getId(entityType).getPath();
        this.spawnPacketId = MotherlodeModule.id("spawn_" + entityName);
        this.bounceLimit = bounceLimit;
    }
    
    public ExplosiveItemEntity(EntityType<? extends ExplosiveItemEntity> entityType, World world) {
        this(entityType, world, 1);
    }
    
    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        
        if (!this.world.isClient) {
            this.world.createExplosion(this, getX(), getY(), getZ(), 3.0f, Explosion.DestructionType.BREAK);
            this.world.sendEntityStatus(this, (byte) 3);
            this.remove(RemovalReason.KILLED);
        }
    }
    
    @Override
    public void initDataTracker() {
        this.dataTracker.startTracking(FUSE_LIT, false);
        this.dataTracker.startTracking(BOUNCE_COUNT, 0);
        this.dataTracker.startTracking(BOUNCE_LIMIT, bounceLimit);
    }
    
    @Override
    public Packet<?> createSpawnPacket() {
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        
        packet.writeBlockPos(this.getBlockPos());
        packet.writeInt(getId());
        packet.writeUuid(getUuid());
        
        return ServerSidePacketRegistry.INSTANCE.toPacket(spawnPacketId, packet);
    }
}
