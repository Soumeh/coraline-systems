package net.zharok01.coralinesystems.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface AccessGameRenderer {

    @Accessor("postEffect")
    void photoMode$setPostChain(PostChain postProcessor);

    @Accessor("effectActive")
    void photoMode$setEffectActive(boolean enabled);

}
