package net.zharok01.coralinesystems.mixin;

import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PostChain.class)
public interface AccessPostEffectProcessor {

    @Accessor("resourceManager")
	ResourceManager photoMode$getResourceManager();

    @Accessor("passes")
    List<PostPass> photoMode$getPasses();

}
