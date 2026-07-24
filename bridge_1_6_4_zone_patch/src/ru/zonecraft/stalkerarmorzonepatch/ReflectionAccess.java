package ru.zonecraft.stalkerarmorzonepatch;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

final class ReflectionAccess {
    private ReflectionAccess() {
    }

    static Object getChestArmor(Object player) {
        if (player == null) {
            return null;
        }
        Object stack = invoke(player, new String[] {"getCurrentArmor", "func_71124_b"}, new Class[] {Integer.TYPE}, new Object[] {Integer.valueOf(2)});
        if (stack != null) {
            return stack;
        }
        Object inventory = field(player, new String[] {"inventory", "field_71071_by"});
        Object armorArray = field(inventory, new String[] {"armorInventory", "field_70460_b"});
        if (armorArray != null && armorArray.getClass().isArray() && Array.getLength(armorArray) > 2) {
            return Array.get(armorArray, 2);
        }
        return null;
    }

    static int getItemId(Object itemStack) {
        Object item = getItem(itemStack);
        Number id = numberField(item, new String[] {"itemID", "field_77779_bT"});
        if (id != null) {
            return id.intValue();
        }
        Number stackId = numberField(itemStack, new String[] {"itemID", "field_77993_c"});
        return stackId == null ? -1 : stackId.intValue();
    }

    static String getItemIdentity(Object itemStack) {
        if (itemStack == null) {
            return null;
        }
        Object item = getItem(itemStack);
        Object name = null;
        if (item != null) {
            name = invokeCompatible(item, new String[] {"getUnlocalizedName", "func_77667_c"}, new Object[] {itemStack});
            if (!(name instanceof String)) {
                name = invoke(item, new String[] {"getUnlocalizedName", "func_77658_a"}, new Class[0], new Object[0]);
            }
        }
        String className = item == null ? itemStack.getClass().getName() : item.getClass().getName();
        return (name instanceof String ? String.valueOf(name) : "") + "|" + className + "|" + getItemId(itemStack);
    }

    static boolean isGlbSuit(Object itemStack) {
        if (itemStack == null) {
            return false;
        }
        Object item = getItem(itemStack);
        Class<?> type = item == null ? itemStack.getClass() : item.getClass();
        while (type != null) {
            String name = type.getName().toLowerCase(Locale.ROOT);
            if (name.indexOf("ru.stalcraft.glbarmor") >= 0 || name.indexOf("itemstalcraftsuit") >= 0) {
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }

    static String getStackDisplayName(Object itemStack) {
        if (itemStack == null) {
            return "";
        }
        Object value = invoke(itemStack, new String[] {"getDisplayName", "func_82833_r"}, new Class[0], new Object[0]);
        if (value instanceof String) {
            return String.valueOf(value);
        }
        Object item = getItem(itemStack);
        value = invokeCompatible(item, new String[] {"getItemDisplayName", "func_77653_i"}, new Object[] {itemStack});
        return value instanceof String ? String.valueOf(value) : "";
    }

    static String getDamageType(Object damageSource) {
        Object value = invoke(damageSource, new String[] {"getDamageType", "func_76355_l"}, new Class[0], new Object[0]);
        if (!(value instanceof String)) {
            value = field(damageSource, new String[] {"damageType", "field_76373_n"});
        }
        return value == null ? "" : String.valueOf(value);
    }

    static Object getDamageAttacker(Object damageSource) {
        return invoke(damageSource, new String[] {"getEntity", "func_76346_g"}, new Class[0], new Object[0]);
    }

    static Object getDamageSourceEntity(Object damageSource) {
        return invoke(damageSource, new String[] {"getSourceOfDamage", "func_76364_f"}, new Class[0], new Object[0]);
    }

    static double[] getPosition(Object entity, boolean previous) {
        if (entity == null) {
            return null;
        }
        String[] xNames = previous ? new String[] {"prevPosX", "field_70169_q"} : new String[] {"posX", "field_70165_t"};
        String[] yNames = previous ? new String[] {"prevPosY", "field_70167_r"} : new String[] {"posY", "field_70163_u"};
        String[] zNames = previous ? new String[] {"prevPosZ", "field_70166_s"} : new String[] {"posZ", "field_70161_v"};
        Number x = numberField(entity, xNames);
        Number y = numberField(entity, yNames);
        Number z = numberField(entity, zNames);
        if (x == null || y == null || z == null) {
            return null;
        }
        return new double[] {x.doubleValue(), y.doubleValue(), z.doubleValue()};
    }

    static double[] getMotion(Object entity) {
        Number x = numberField(entity, new String[] {"motionX", "field_70159_w"});
        Number y = numberField(entity, new String[] {"motionY", "field_70181_x"});
        Number z = numberField(entity, new String[] {"motionZ", "field_70179_y"});
        if (x == null || y == null || z == null) {
            return null;
        }
        return new double[] {x.doubleValue(), y.doubleValue(), z.doubleValue()};
    }

    static double getEyeHeight(Object entity) {
        Object value = invoke(entity, new String[] {"getEyeHeight", "func_70047_e"}, new Class[0], new Object[0]);
        return value instanceof Number ? ((Number) value).doubleValue() : 1.62D;
    }

    static double[] getLookVector(Object entity) {
        Object vector = invoke(entity, new String[] {"getLookVec", "func_70040_Z"}, new Class[0], new Object[0]);
        if (vector == null) {
            vector = invoke(entity, new String[] {"getLook", "func_70676_i"}, new Class[] {Float.TYPE}, new Object[] {Float.valueOf(1.0F)});
        }
        return readVector(vector);
    }

    static Box getBoundingBox(Object entity) {
        Object box = field(entity, new String[] {"boundingBox", "field_70121_D"});
        if (box == null) {
            return null;
        }
        Number minX = numberField(box, new String[] {"minX", "field_72340_a"});
        Number minY = numberField(box, new String[] {"minY", "field_72338_b"});
        Number minZ = numberField(box, new String[] {"minZ", "field_72339_c"});
        Number maxX = numberField(box, new String[] {"maxX", "field_72336_d"});
        Number maxY = numberField(box, new String[] {"maxY", "field_72337_e"});
        Number maxZ = numberField(box, new String[] {"maxZ", "field_72334_f"});
        if (minX == null || minY == null || minZ == null || maxX == null || maxY == null || maxZ == null) {
            return null;
        }
        return new Box(minX.doubleValue(), minY.doubleValue(), minZ.doubleValue(), maxX.doubleValue(), maxY.doubleValue(), maxZ.doubleValue());
    }

    static double getYaw(Object entity) {
        Number value = numberField(entity, new String[] {"rotationYaw", "field_70177_z"});
        return value == null ? 0.0D : value.doubleValue();
    }

    static boolean isPlayerLike(Object entity) {
        if (entity == null) {
            return false;
        }
        Class<?> type = entity.getClass();
        while (type != null) {
            String name = type.getName();
            if (name.endsWith("EntityPlayer") || name.endsWith("EntityPlayerMP") || name.endsWith("EntityPlayerSP")) {
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }

    static boolean isProjectileLike(Object entity) {
        if (entity == null) {
            return false;
        }
        String name = entity.getClass().getName().toLowerCase(Locale.ROOT);
        return name.indexOf("bullet") >= 0 || name.indexOf("projectile") >= 0 || name.indexOf("arrow") >= 0 || name.indexOf("grenade") >= 0 || name.indexOf("rocket") >= 0;
    }

    private static Object getItem(Object itemStack) {
        if (itemStack == null) {
            return null;
        }
        Object item = invoke(itemStack, new String[] {"getItem", "func_77973_b"}, new Class[0], new Object[0]);
        if (item == null) {
            item = field(itemStack, new String[] {"item", "field_151002_e"});
        }
        return item;
    }

    private static double[] readVector(Object vector) {
        if (vector == null) {
            return null;
        }
        Number x = numberField(vector, new String[] {"xCoord", "field_72450_a"});
        Number y = numberField(vector, new String[] {"yCoord", "field_72448_b"});
        Number z = numberField(vector, new String[] {"zCoord", "field_72449_c"});
        if (x == null || y == null || z == null) {
            return null;
        }
        return new double[] {x.doubleValue(), y.doubleValue(), z.doubleValue()};
    }

    private static Number numberField(Object target, String[] names) {
        Object value = field(target, names);
        return value instanceof Number ? (Number) value : null;
    }

    private static Object field(Object target, String[] names) {
        if (target == null) {
            return null;
        }
        Class<?> type = target.getClass();
        while (type != null) {
            int index;
            for (index = 0; index < names.length; index++) {
                try {
                    Field field = type.getDeclaredField(names[index]);
                    field.setAccessible(true);
                    return field.get(target);
                } catch (Throwable ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    private static Object invoke(Object target, String[] names, Class[] parameterTypes, Object[] arguments) {
        if (target == null) {
            return null;
        }
        Class<?> type = target.getClass();
        while (type != null) {
            int index;
            for (index = 0; index < names.length; index++) {
                try {
                    Method method = type.getDeclaredMethod(names[index], parameterTypes);
                    method.setAccessible(true);
                    return method.invoke(target, arguments);
                } catch (Throwable ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    private static Object invokeCompatible(Object target, String[] names, Object[] arguments) {
        if (target == null) {
            return null;
        }
        Class<?> type = target.getClass();
        while (type != null) {
            Method[] methods = type.getDeclaredMethods();
            int methodIndex;
            for (methodIndex = 0; methodIndex < methods.length; methodIndex++) {
                Method method = methods[methodIndex];
                if (!contains(names, method.getName()) || method.getParameterTypes().length != arguments.length) {
                    continue;
                }
                try {
                    method.setAccessible(true);
                    return method.invoke(target, arguments);
                } catch (Throwable ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    private static boolean contains(String[] values, String candidate) {
        int index;
        for (index = 0; index < values.length; index++) {
            if (values[index].equals(candidate)) {
                return true;
            }
        }
        return false;
    }

    static final class Box {
        final double minX;
        final double minY;
        final double minZ;
        final double maxX;
        final double maxY;
        final double maxZ;

        Box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
        }

        Box expand(double value) {
            return new Box(minX - value, minY - value, minZ - value, maxX + value, maxY + value, maxZ + value);
        }
    }
}
