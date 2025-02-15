package com.github.Gorden121.the_banner_capes.mixin;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import com.github.Gorden121.the_banner_capes.client.render.BannerCapeFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float f) {
        super(ctx, model, f);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        BannerCapes.log(Level.DEBUG, "PlayerEntityRenderer mix'd-in!");
        this.addFeature(new BannerCapeFeatureRenderer(this));
    }
}
