package ru.zonecraft.stalkerarmorzonepatch;

final class HitZoneResolver {
    private HitZoneResolver() {
    }

    static HitZone resolve(Object victim, Object damageSource, String damageType) {
        String lowerType = damageType == null ? "" : damageType.toLowerCase();
        if (containsAny(lowerType, new String[] {"headshot", "head_shot", "head", "helmet"})) {
            return HitZone.HEAD;
        }
        if (containsAny(lowerType, new String[] {"limb", "arm", "leg", "foot", "hand"})) {
            return HitZone.LIMB;
        }

        ReflectionAccess.Box box = ReflectionAccess.getBoundingBox(victim);
        if (box == null || box.maxY <= box.minY) {
            return HitZone.BODY;
        }

        Object attacker = ReflectionAccess.getDamageAttacker(damageSource);
        Object directSource = ReflectionAccess.getDamageSourceEntity(damageSource);
        double[] hit = null;

        if (directSource != null && directSource != attacker && ReflectionAccess.isProjectileLike(directSource)) {
            hit = projectileHit(directSource, box.expand(PatchSettings.rayBoxExpansion));
        }
        if (hit == null && attacker != null) {
            hit = hitscanHit(attacker, box.expand(PatchSettings.rayBoxExpansion));
        }
        if (hit == null && directSource != null) {
            hit = projectileHit(directSource, box.expand(PatchSettings.rayBoxExpansion));
        }
        if (hit == null) {
            return HitZone.BODY;
        }

        return classify(victim, box, hit);
    }

    private static double[] projectileHit(Object projectile, ReflectionAccess.Box box) {
        double[] current = ReflectionAccess.getPosition(projectile, false);
        double[] previous = ReflectionAccess.getPosition(projectile, true);
        if (current == null) {
            return null;
        }
        double[] direction = null;
        double[] origin = previous;
        if (previous != null) {
            direction = subtract(current, previous);
        }
        if (direction == null || lengthSquared(direction) < 1.0E-8D) {
            direction = ReflectionAccess.getMotion(projectile);
            origin = current;
        }
        if (direction == null || lengthSquared(direction) < 1.0E-8D) {
            return current;
        }
        direction = normalize(direction);
        return intersectOrClosest(origin, direction, box, 4.0D);
    }

    private static double[] hitscanHit(Object attacker, ReflectionAccess.Box box) {
        double[] origin = ReflectionAccess.getPosition(attacker, false);
        double[] direction = ReflectionAccess.getLookVector(attacker);
        if (origin == null || direction == null || lengthSquared(direction) < 1.0E-8D) {
            return null;
        }
        origin[1] += ReflectionAccess.getEyeHeight(attacker);
        direction = normalize(direction);
        return intersectOrClosest(origin, direction, box, 192.0D);
    }

    private static double[] intersectOrClosest(double[] origin, double[] direction, ReflectionAccess.Box box, double maximumDistance) {
        double tMin = 0.0D;
        double tMax = maximumDistance;
        double[] xRange = axisRange(origin[0], direction[0], box.minX, box.maxX);
        if (xRange == null) {
            return closest(origin, direction, box, maximumDistance);
        }
        tMin = Math.max(tMin, xRange[0]);
        tMax = Math.min(tMax, xRange[1]);

        double[] yRange = axisRange(origin[1], direction[1], box.minY, box.maxY);
        if (yRange == null) {
            return closest(origin, direction, box, maximumDistance);
        }
        tMin = Math.max(tMin, yRange[0]);
        tMax = Math.min(tMax, yRange[1]);

        double[] zRange = axisRange(origin[2], direction[2], box.minZ, box.maxZ);
        if (zRange == null) {
            return closest(origin, direction, box, maximumDistance);
        }
        tMin = Math.max(tMin, zRange[0]);
        tMax = Math.min(tMax, zRange[1]);

        if (tMax >= tMin && tMax >= 0.0D && tMin <= maximumDistance) {
            double t = Math.max(0.0D, tMin);
            return point(origin, direction, t);
        }
        return closest(origin, direction, box, maximumDistance);
    }

    private static double[] axisRange(double origin, double direction, double minimum, double maximum) {
        if (Math.abs(direction) < 1.0E-9D) {
            return origin >= minimum && origin <= maximum ? new double[] {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY} : null;
        }
        double first = (minimum - origin) / direction;
        double second = (maximum - origin) / direction;
        if (first > second) {
            double temporary = first;
            first = second;
            second = temporary;
        }
        return new double[] {first, second};
    }

    private static double[] closest(double[] origin, double[] direction, ReflectionAccess.Box box, double maximumDistance) {
        double centerX = (box.minX + box.maxX) * 0.5D;
        double centerY = (box.minY + box.maxY) * 0.5D;
        double centerZ = (box.minZ + box.maxZ) * 0.5D;
        double t = (centerX - origin[0]) * direction[0] + (centerY - origin[1]) * direction[1] + (centerZ - origin[2]) * direction[2];
        t = Math.max(0.0D, Math.min(maximumDistance, t));
        double[] point = point(origin, direction, t);
        point[0] = clamp(point[0], box.minX, box.maxX);
        point[1] = clamp(point[1], box.minY, box.maxY);
        point[2] = clamp(point[2], box.minZ, box.maxZ);
        return point;
    }

    private static HitZone classify(Object victim, ReflectionAccess.Box box, double[] hit) {
        double height = box.maxY - box.minY;
        double normalizedY = clamp((hit[1] - box.minY) / height, 0.0D, 1.0D);
        if (normalizedY >= PatchSettings.headThreshold) {
            return HitZone.HEAD;
        }
        if (normalizedY <= PatchSettings.legThreshold) {
            return HitZone.LIMB;
        }

        if (normalizedY >= PatchSettings.armMinimumHeight && normalizedY <= PatchSettings.armMaximumHeight) {
            double centerX = (box.minX + box.maxX) * 0.5D;
            double centerZ = (box.minZ + box.maxZ) * 0.5D;
            double deltaX = hit[0] - centerX;
            double deltaZ = hit[2] - centerZ;
            double yaw = Math.toRadians(ReflectionAccess.getYaw(victim));
            double localSide = Math.abs(deltaX * Math.cos(yaw) + deltaZ * Math.sin(yaw));
            double halfWidth = Math.max(box.maxX - box.minX, box.maxZ - box.minZ) * 0.5D;
            if (halfWidth > 0.0D && localSide / halfWidth >= PatchSettings.armSideThreshold) {
                return HitZone.LIMB;
            }
        }
        return HitZone.BODY;
    }

    private static boolean containsAny(String text, String[] values) {
        int index;
        for (index = 0; index < values.length; index++) {
            if (text.indexOf(values[index]) >= 0) {
                return true;
            }
        }
        return false;
    }

    private static double[] point(double[] origin, double[] direction, double distance) {
        return new double[] {
            origin[0] + direction[0] * distance,
            origin[1] + direction[1] * distance,
            origin[2] + direction[2] * distance
        };
    }

    private static double[] subtract(double[] first, double[] second) {
        return new double[] {first[0] - second[0], first[1] - second[1], first[2] - second[2]};
    }

    private static double[] normalize(double[] vector) {
        double length = Math.sqrt(lengthSquared(vector));
        return length < 1.0E-9D ? vector : new double[] {vector[0] / length, vector[1] / length, vector[2] / length};
    }

    private static double lengthSquared(double[] vector) {
        return vector == null ? 0.0D : vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2];
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
