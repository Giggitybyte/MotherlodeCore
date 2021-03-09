package motherlode.spelunky;

import motherlode.base.Motherlode;
import motherlode.base.api.Registerable;
import motherlode.base.api.assets.CommonAssets;
import motherlode.spelunky.item.BombItem;
import motherlode.spelunky.item.DynamiteItem;
import motherlode.spelunky.item.StickyDynamiteItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class MotherlodeSpelunkyItems {
    public static final Item BOMB = register("bomb", new BombItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(16)));
    public static final Item DYNAMITE = register("dynamite", new DynamiteItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(16)));
    public static final Item DYNAMITE_STICKY = register("dynamite_sticky", new StickyDynamiteItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(16)));
    public static final Item SLIME_BUCKET = register("slime_bucket", new BucketItem(MotherlodeSpelunkyFluids.STILL_SLIME, new Item.Settings().group(ItemGroup.MISC)));
    
    private static <T extends Item> T register(String name, T item) {
        return Motherlode.register(
                Registerable.item(item),
                MotherlodeModule.id(name),
                item,
                CommonAssets.DEFAULT_ITEM_MODEL
        );
    }
    
    public static void init() {
    }
}
