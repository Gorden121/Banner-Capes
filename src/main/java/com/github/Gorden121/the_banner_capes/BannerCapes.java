package com.github.Gorden121.the_banner_capes;

import com.github.Gorden121.the_banner_capes.compat.GeneralModCompat;
import com.github.Gorden121.the_banner_capes.config.BannerCapesConfig;
import com.github.Gorden121.the_banner_capes.data.BannerCapeMaterialsData;
import com.github.Gorden121.the_banner_capes.data.BannerCapeSimpleSynchronousResourceReloadListener;
import com.github.Gorden121.the_banner_capes.item.BannerCapeItem;
import com.github.Gorden121.the_banner_capes.recipe.BannerCapeShapedRecipe;
import dev.emi.trinkets.api.TrinketItem;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BannerCapes implements ModInitializer, GeneralModCompat {
    // nasty, dirty, filthy fix to have trinkets not get eaten
    // up all at once or on failure
    public static final DispenserBehavior STACKABLE_TRINKET_DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            BlockPos pos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
            List<LivingEntity> entities = pointer.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR);
            if (!entities.isEmpty()) {
                LivingEntity entity = entities.get(0);
                if (entity instanceof PlayerEntity) {
                    if (TrinketItem.equipItem((PlayerEntity) entity,stack)) {
                        stack.decrement(1);
                        return stack;
                    }
                }
            }
            return super.dispenseSilently(pointer, stack);
        }
    };

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "the_banner_capes";
    public static final String MOD_NAME = "The Banner Capes";
    public static final String MOD_VERSION = "2.0.0-1.17.1";
    public static final String ConfigVersionInternal = "V2";

    public static final Item BANNER_CAPE = new BannerCapeItem();


    public static RecipeSerializer<BannerCapeShapedRecipe> BANNER_CAPE_SHAPED_SERIALIZER = Registry.register(
            Registry.RECIPE_SERIALIZER,
            MOD_ID + ":crafting_bannercape_shaped",
            new BannerCapeShapedRecipe.Serializer()
    );

    public static BannerCapesConfig config() {
        return AutoConfig.getConfigHolder(BannerCapesConfig.class).get();
    }
    public static BannerCapeMaterialsData bannerCapeMaterialsData = new BannerCapeMaterialsData();

    @Override
    public void onInitialize() {

        log(Level.INFO, "Initializing " + MOD_NAME + " (" + MOD_ID + ") " + MOD_VERSION);

        AutoConfig.register(BannerCapesConfig.class, GsonConfigSerializer::new);
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BannerCapeSimpleSynchronousResourceReloadListener());

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "banner_cape"), BANNER_CAPE);

        /*try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter("output.json");
            writer.write(gson.toJson(bannerCapeMaterialsData.getMinimumDecorationMaterials()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }


    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}