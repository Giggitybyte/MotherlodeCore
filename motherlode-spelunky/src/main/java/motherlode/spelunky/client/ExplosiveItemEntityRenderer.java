package motherlode.spelunky.client;

import motherlode.spelunky.MotherlodeModule;
import motherlode.spelunky.entity.ExplosiveItemEntity;
import motherlode.spelunky.item.ExplosiveItem;
import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ExplosiveItemEntityRenderer extends EntityRenderer<ExplosiveItemEntity> {
    private final ItemStack explosiveItem;
    private final Identifier modelTexture;
    
    public ExplosiveItemEntityRenderer(EntityRendererFactory.Context ctx, Item item) {
        super(ctx);
        
        this.modelTexture = ((ExplosiveItem) item).getTexturePath();
        this.explosiveItem = new ItemStack(item);
    }
    
    @Override
    public void render(ExplosiveItemEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                explosiveItem,
                ModelTransformation.Mode.GROUND,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                32
        );
        
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
    
    @Override
    public Identifier getTexture(ExplosiveItemEntity entity) {
        return modelTexture;
    }
}
