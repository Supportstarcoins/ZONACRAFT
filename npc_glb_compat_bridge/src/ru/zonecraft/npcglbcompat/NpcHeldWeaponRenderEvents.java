package ru.zonecraft.npcglbcompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Renders a held StalkerMod firearm after the living model has finished.
 *
 * CustomNPCs normally anchors held items through bipedRightArm.postRender().
 * When the one-piece GLB suit hides the original arm/body, that transform is
 * skipped and the firearm becomes invisible. This handler reconstructs the
 * entity matrix, temporarily exposes only the arm transform, and invokes the
 * normal Minecraft/Forge ItemRenderer. No StalkerMod renderer is replaced.
 */
public final class NpcHeldWeaponRenderEvents {
    private static boolean failureLogged;

    @ForgeSubscribe(priority = EventPriority.LOWEST)
    public void onRenderLivingPost(RenderLivingEvent.Post event) {
        Object living = event == null ? null : event.entity;
        Object renderer = event == null ? null : event.renderer;
        if (living == null || renderer == null || isPlayer(living)) {
            return;
        }

        Object suit = CompatReflection.findSuit(living);
        Object weaponStack = CompatReflection.findHeldWeapon(living);
        if (suit == null || weaponStack == null) {
            return;
        }

        // When the original arm is intentionally visible, CustomNPCs can use
        // its normal held-item renderer and a second pass is unnecessary.
        if (originalArmsVisible(suit)) {
            return;
        }

        ModelBiped model = findBiped(renderer);
        ModelRenderer rightArm = findRightArm(model);
        if (model == null || rightArm == null) {
            logFailure("Cannot locate the active ModelBiped/right arm for "
                    + renderer.getClass().getName(), null);
            return;
        }

        renderWeapon(living, renderer, rightArm, weaponStack,
                event.x, event.y, event.z, getPartialTicks());
    }

    private static void renderWeapon(Object living, Object renderer,
                                     ModelRenderer rightArm, Object weaponStack,
                                     double x, double y, double z,
                                     float partialTicks) {
        boolean oldShow = CompatReflection.booleanField(rightArm,
                new String[] {"showModel", "field_78806_j"}, true);
        boolean oldHidden = CompatReflection.booleanField(rightArm,
                new String[] {"isHidden", "field_78807_k"}, false);
        boolean oldRescale = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);

        GL11.glPushMatrix();
        try {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float) x, (float) y, (float) z);

            float bodyYaw = interpolateRotation(
                    CompatReflection.floatField(living,
                            new String[] {"prevRenderYawOffset", "field_70760_ar"}, 0.0F),
                    CompatReflection.floatField(living,
                            new String[] {"renderYawOffset", "field_70761_aq"}, 0.0F),
                    partialTicks);
            float age = CompatReflection.floatField(living,
                    new String[] {"ticksExisted", "field_70173_aa"}, 0.0F)
                    + partialTicks;

            if (!invokeVoid(renderer,
                    new String[] {"rotateCorpse", "func_77043_a"},
                    new Object[] {living, Float.valueOf(age),
                            Float.valueOf(bodyYaw), Float.valueOf(partialTicks)})) {
                GL11.glRotatef(180.0F - bodyYaw, 0.0F, 1.0F, 0.0F);
            }

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            invokeVoid(renderer,
                    new String[] {"preRenderCallback", "func_77041_b"},
                    new Object[] {living, Float.valueOf(partialTicks)});
            GL11.glTranslatef(0.0F, -1.5078125F, 0.0F);

            CompatReflection.setBoolean(rightArm,
                    new String[] {"showModel", "field_78806_j"}, true);
            CompatReflection.setBoolean(rightArm,
                    new String[] {"isHidden", "field_78807_k"}, false);
            invokeVoid(rightArm,
                    new String[] {"postRender", "func_78794_c"},
                    new Object[] {Float.valueOf(0.0625F)});

            // Same hand-space placement used by RenderNPCHumanMale.
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
            applyHeldItemTransform(weaponStack);

            Object renderManager = CompatReflection.field(renderer,
                    new String[] {"renderManager", "field_76990_c"});
            if (renderManager == null) {
                try {
                    Class<?> managerClass = Class.forName(
                            "net.minecraft.client.renderer.entity.RenderManager");
                    renderManager = CompatReflection.staticField(managerClass,
                            new String[] {"instance", "field_78727_a"});
                } catch (Throwable ignored) {
                }
            }
            Object itemRenderer = CompatReflection.field(renderManager,
                    new String[] {"itemRenderer", "field_78721_f"});
            if (itemRenderer == null) {
                logFailure("Cannot locate RenderManager.itemRenderer.", null);
                return;
            }

            Object result = CompatReflection.invokeCompatible(itemRenderer,
                    new String[] {"renderItem", "func_78443_a"},
                    new Object[] {living, weaponStack, Integer.valueOf(0)});
            // A void method returns null, so verify its existence separately.
            Method method = CompatReflection.findCompatibleMethod(
                    itemRenderer.getClass(),
                    new String[] {"renderItem", "func_78443_a"},
                    new Object[] {living, weaponStack, Integer.valueOf(0)},
                    false);
            if (method == null) {
                logFailure("Cannot locate ItemRenderer.renderItem for the NPC weapon.", null);
            }
        } catch (Throwable error) {
            logFailure("Held Stalker weapon render failed.", error);
        } finally {
            CompatReflection.setBoolean(rightArm,
                    new String[] {"showModel", "field_78806_j"}, oldShow);
            CompatReflection.setBoolean(rightArm,
                    new String[] {"isHidden", "field_78807_k"}, oldHidden);
            if (oldRescale) {
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            } else {
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    private static void applyHeldItemTransform(Object stack) {
        Object item = CompatReflection.getItem(stack);
        boolean full3D = Boolean.TRUE.equals(CompatReflection.invoke(item,
                new String[] {"isFull3D", "func_77662_d"},
                new Class[0], new Object[0]));
        if (full3D) {
            float scale = 0.625F;
            boolean rotate = Boolean.TRUE.equals(CompatReflection.invoke(item,
                    new String[] {"shouldRotateAroundWhenRendering", "func_77629_n_"},
                    new Class[0], new Object[0]));
            if (rotate) {
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(0.0F, -0.125F, 0.0F);
            }
            GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
            GL11.glScalef(scale, -scale, scale);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        } else {
            float scale = 0.375F;
            GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
            GL11.glScalef(scale, scale, scale);
            GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
        }
    }

    private static ModelBiped findBiped(Object renderer) {
        Class<?> type = renderer.getClass();
        while (type != null && type != Object.class) {
            Field[] fields = type.getDeclaredFields();
            int index;
            for (index = 0; index < fields.length; index++) {
                try {
                    fields[index].setAccessible(true);
                    Object value = fields[index].get(renderer);
                    if (value instanceof ModelBiped) {
                        return (ModelBiped) value;
                    }
                } catch (Throwable ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    private static ModelRenderer findRightArm(ModelBiped model) {
        if (model == null) {
            return null;
        }
        Object arm = CompatReflection.field(model,
                new String[] {"bipedRightArm", "field_78112_f"});
        return arm instanceof ModelRenderer ? (ModelRenderer) arm : null;
    }

    private static boolean originalArmsVisible(Object suitStack) {
        Object item = CompatReflection.getItem(suitStack);
        Object definition = CompatReflection.invoke(item,
                new String[] {"getDefinition"}, new Class[0], new Object[0]);
        return CompatReflection.booleanField(definition,
                new String[] {"showOriginalArms"}, false);
    }

    private static float getPartialTicks() {
        try {
            Class<?> minecraftClass = Class.forName("net.minecraft.client.Minecraft");
            Method getter = CompatReflection.findCompatibleMethod(
                    minecraftClass,
                    new String[] {"getMinecraft", "func_71410_x"},
                    new Object[0], true);
            Object minecraft = getter == null ? null : getter.invoke(null, new Object[0]);
            Object timer = CompatReflection.field(minecraft,
                    new String[] {"timer", "field_71428_T"});
            return CompatReflection.floatField(timer,
                    new String[] {"renderPartialTicks", "field_74281_c"}, 0.0F);
        } catch (Throwable ignored) {
            return 0.0F;
        }
    }

    private static boolean invokeVoid(Object target, String[] names, Object[] arguments) {
        if (target == null) {
            return false;
        }
        Method method = CompatReflection.findCompatibleMethod(
                target.getClass(), names, arguments, false);
        if (method == null) {
            return false;
        }
        try {
            method.invoke(target, arguments);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static float interpolateRotation(float previous, float current,
                                             float partialTicks) {
        float difference = current - previous;
        while (difference < -180.0F) difference += 360.0F;
        while (difference >= 180.0F) difference -= 360.0F;
        return previous + partialTicks * difference;
    }

    private static boolean isPlayer(Object living) {
        Class<?> type = living.getClass();
        while (type != null && type != Object.class) {
            if (type.getName().indexOf("EntityPlayer") >= 0) {
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }

    private static void logFailure(String message, Throwable error) {
        if (failureLogged) {
            return;
        }
        failureLogged = true;
        System.err.println("[Zonecraft NPC GLB Compat] " + message);
        if (error != null) {
            error.printStackTrace();
        }
    }
}
