package ru.zonecraft.armorbootfix;

import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

/**
 * Applies one universal render-space floor correction to every one-piece GLB
 * suit. The translation begins in RenderLivingEvent.Pre and is removed at the
 * very end of RenderLivingEvent.Post, so GLB armor rendered by another Post
 * handler and the reconstructed held weapon receive the same correction.
 */
public final class BootGroundRenderEvents {
    private final Map<Object, Float> activeShift =
            new IdentityHashMap<Object, Float>();

    @ForgeSubscribe(priority = EventPriority.LOWEST)
    public void beforeLivingRender(RenderLivingEvent.Pre event) {
        if (!BootFixSettings.enabled || event == null || event.entity == null) {
            return;
        }
        if (activeShift.containsKey(event.entity)) {
            return;
        }
        if (!SuitDetector.wearsGlbSuit(event.entity)) {
            return;
        }
        if (!BootFixSettings.applyWhileAirborne
                && !SuitDetector.isOnGround(event.entity)) {
            return;
        }

        float lift = BootFixSettings.baseLiftBlocks;
        if (SuitDetector.isSneaking(event.entity)) {
            lift += BootFixSettings.sneakingExtraBlocks;
        }
        if (lift <= 0.0F) {
            return;
        }

        GL11.glTranslatef(0.0F, lift, 0.0F);
        activeShift.put(event.entity, Float.valueOf(lift));
    }

    @ForgeSubscribe(priority = EventPriority.LOWEST)
    public void afterLivingRender(RenderLivingEvent.Post event) {
        if (event == null || event.entity == null) {
            return;
        }
        Float lift = activeShift.remove(event.entity);
        if (lift != null && lift.floatValue() != 0.0F) {
            GL11.glTranslatef(0.0F, -lift.floatValue(), 0.0F);
        }
    }
}
