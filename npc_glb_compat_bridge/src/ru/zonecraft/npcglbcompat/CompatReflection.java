package ru.zonecraft.npcglbcompat;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

final class CompatReflection {
    private static final String SUIT_CLASS = "ru.stalcraft.glbarmor.item.ItemStalcraftSuit";
    private static final String WEAPON_CLASS = "ru.stalcraft.items.ItemWeapon";

    private CompatReflection() {
    }

    static Object findSuit(Object living) {
        if (living == null) {
            return null;
        }

        Object inventory = field(living, new String[] {"inventory", "field_71071_by"});
        Object stack = findSuitInInventory(inventory);
        if (stack != null) {
            return stack;
        }

        int slot;
        for (slot = 1; slot <= 4; slot++) {
            stack = invoke(living,
                    new String[] {"getCurrentItemOrArmor", "func_71124_b", "func_130225_q"},
                    new Class[] {Integer.TYPE},
                    new Object[] {Integer.valueOf(slot)});
            if (isSuitStack(stack)) {
                return stack;
            }
        }

        for (slot = 0; slot < 4; slot++) {
            stack = invoke(living,
                    new String[] {"getCurrentArmor", "func_71124_b", "func_130225_q"},
                    new Class[] {Integer.TYPE},
                    new Object[] {Integer.valueOf(slot)});
            if (isSuitStack(stack)) {
                return stack;
            }
        }
        return null;
    }

    static Object findHeldWeapon(Object living) {
        Object stack = invoke(living,
                new String[] {"getHeldItem", "func_70694_bm"},
                new Class[0], new Object[0]);
        if (isStalkerWeaponStack(stack)) {
            return stack;
        }

        Object inventory = field(living, new String[] {"inventory", "field_71071_by"});
        stack = invoke(inventory, new String[] {"getWeapon"}, new Class[0], new Object[0]);
        if (isStalkerWeaponStack(stack)) {
            return stack;
        }

        stack = invoke(inventory,
                new String[] {"getStackInSlot", "func_70301_a"},
                new Class[] {Integer.TYPE},
                new Object[] {Integer.valueOf(4)});
        return isStalkerWeaponStack(stack) ? stack : null;
    }

    static boolean isSuitStack(Object stack) {
        return isClassOrSubclass(getItem(stack), SUIT_CLASS);
    }

    static boolean isStalkerWeaponStack(Object stack) {
        return isClassOrSubclass(getItem(stack), WEAPON_CLASS);
    }

    static Object getItem(Object stack) {
        Object item = invoke(stack,
                new String[] {"getItem", "func_77973_b"},
                new Class[0], new Object[0]);
        if (item == null) {
            item = field(stack, new String[] {"item", "field_151002_e"});
        }
        return item;
    }

    static boolean isClassOrSubclass(Object value, String expectedName) {
        if (value == null || expectedName == null) {
            return false;
        }
        Class<?> type = value instanceof Class ? (Class<?>) value : value.getClass();
        while (type != null && type != Object.class) {
            if (expectedName.equals(type.getName())) {
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }

    private static Object findSuitInInventory(Object inventory) {
        if (inventory == null) {
            return null;
        }

        int index;
        for (index = 0; index < 4; index++) {
            Object stack = invoke(inventory,
                    new String[] {"armorItemInSlot"},
                    new Class[] {Integer.TYPE},
                    new Object[] {Integer.valueOf(index)});
            if (isSuitStack(stack)) {
                return stack;
            }
        }

        Object armor = field(inventory,
                new String[] {"armor", "armorInventory", "field_70460_b"});
        if (armor instanceof Map) {
            @SuppressWarnings("rawtypes")
            Map map = (Map) armor;
            for (index = 0; index < 4; index++) {
                Object stack = map.get(Integer.valueOf(index));
                if (isSuitStack(stack)) {
                    return stack;
                }
            }
        }
        if (armor != null && armor.getClass().isArray()) {
            int length = Array.getLength(armor);
            for (index = 0; index < length; index++) {
                Object stack = Array.get(armor, index);
                if (isSuitStack(stack)) {
                    return stack;
                }
            }
        }
        return null;
    }

    static Object field(Object target, String[] names) {
        if (target == null || names == null) {
            return null;
        }
        Class<?> type = target.getClass();
        while (type != null && type != Object.class) {
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

    static Object staticField(Class<?> type, String[] names) {
        while (type != null && type != Object.class) {
            int index;
            for (index = 0; index < names.length; index++) {
                try {
                    Field field = type.getDeclaredField(names[index]);
                    field.setAccessible(true);
                    return field.get(null);
                } catch (Throwable ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    static boolean setField(Object target, String[] names, Object value) {
        if (target == null || names == null) {
            return false;
        }
        Class<?> type = target.getClass();
        while (type != null && type != Object.class) {
            int index;
            for (index = 0; index < names.length; index++) {
                try {
                    Field field = type.getDeclaredField(names[index]);
                    field.setAccessible(true);
                    field.set(target, value);
                    return true;
                } catch (Throwable ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return false;
    }

    static Number numberField(Object target, String[] names) {
        Object value = field(target, names);
        return value instanceof Number ? (Number) value : null;
    }

    static double doubleField(Object target, String[] names, double fallback) {
        Number value = numberField(target, names);
        return value == null ? fallback : value.doubleValue();
    }

    static float floatField(Object target, String[] names, float fallback) {
        Number value = numberField(target, names);
        return value == null ? fallback : value.floatValue();
    }

    static boolean booleanField(Object target, String[] names, boolean fallback) {
        Object value = field(target, names);
        return value instanceof Boolean ? ((Boolean) value).booleanValue() : fallback;
    }

    static void setDouble(Object target, String[] names, double value) {
        setNumeric(target, names, Double.valueOf(value));
    }

    static void setFloat(Object target, String[] names, float value) {
        setNumeric(target, names, Float.valueOf(value));
    }

    static void setBoolean(Object target, String[] names, boolean value) {
        setField(target, names, Boolean.valueOf(value));
    }

    private static boolean setNumeric(Object target, String[] names, Number value) {
        if (target == null || names == null) {
            return false;
        }
        Class<?> type = target.getClass();
        while (type != null && type != Object.class) {
            int index;
            for (index = 0; index < names.length; index++) {
                try {
                    Field field = type.getDeclaredField(names[index]);
                    field.setAccessible(true);
                    Class<?> fieldType = field.getType();
                    if (fieldType == Double.TYPE || fieldType == Double.class) {
                        field.setDouble(target, value.doubleValue());
                    } else if (fieldType == Float.TYPE || fieldType == Float.class) {
                        field.setFloat(target, value.floatValue());
                    } else if (fieldType == Integer.TYPE || fieldType == Integer.class) {
                        field.setInt(target, value.intValue());
                    } else {
                        field.set(target, value);
                    }
                    return true;
                } catch (Throwable ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return false;
    }

    static Object invoke(Object target, String[] names,
                         Class[] parameterTypes, Object[] arguments) {
        if (target == null) {
            return null;
        }
        Class<?> type = target.getClass();
        while (type != null && type != Object.class) {
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

    static Method findCompatibleMethod(Class<?> start, String[] names,
                                       Object[] arguments, boolean requireStatic) {
        Class<?> type = start;
        while (type != null && type != Object.class) {
            Method[] methods = type.getDeclaredMethods();
            int methodIndex;
            for (methodIndex = 0; methodIndex < methods.length; methodIndex++) {
                Method method = methods[methodIndex];
                if (names != null && names.length > 0 && !contains(names, method.getName())) {
                    continue;
                }
                if (requireStatic != Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length != arguments.length) {
                    continue;
                }
                boolean compatible = true;
                int argumentIndex;
                for (argumentIndex = 0; argumentIndex < parameters.length; argumentIndex++) {
                    Object argument = arguments[argumentIndex];
                    if (argument == null) {
                        continue;
                    }
                    Class<?> parameter = wrap(parameters[argumentIndex]);
                    if (!parameter.isAssignableFrom(argument.getClass())) {
                        compatible = false;
                        break;
                    }
                }
                if (compatible) {
                    try {
                        method.setAccessible(true);
                    } catch (Throwable ignored) {
                    }
                    return method;
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    static Object invokeCompatible(Object target, String[] names, Object[] arguments) {
        if (target == null) {
            return null;
        }
        Method method = findCompatibleMethod(target.getClass(), names, arguments, false);
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(target, arguments);
        } catch (Throwable ignored) {
            return null;
        }
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

    private static Class<?> wrap(Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        }
        if (type == Integer.TYPE) return Integer.class;
        if (type == Float.TYPE) return Float.class;
        if (type == Double.TYPE) return Double.class;
        if (type == Boolean.TYPE) return Boolean.class;
        if (type == Long.TYPE) return Long.class;
        if (type == Short.TYPE) return Short.class;
        if (type == Byte.TYPE) return Byte.class;
        if (type == Character.TYPE) return Character.class;
        return type;
    }
}
