package net.zharok01.coralinesystems.mixin;

import com.google.common.collect.ImmutableMap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class PhotoModeCommonMixinPlugin implements IMixinConfigPlugin {

    public static boolean HAS_SODIUM;

    static {
        try {
            MixinService.getService().getBytecodeProvider().getClassNode("me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer");
            HAS_SODIUM = true;
        } catch (Throwable t) {
            HAS_SODIUM = false;
        }
    }

    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
        "net.zharok01.coralinesystems.mixin.MixinWorldRendererVanilla", () -> !HAS_SODIUM,
        "net.zharok01.coralinesystems.mixin.compat.MixinDefaultChunkRenderer", () -> HAS_SODIUM,
        "net.zharok01.coralinesystems.mixin.compat.MixinSodiumWorldRenderer", () -> HAS_SODIUM
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

}
