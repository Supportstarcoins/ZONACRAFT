package ru.stalcraft.port.anomaly;

public enum AnomalyType {
    ELECTRA(2.2D, 1.0D, 6.0F, 30, 16, 0.55D, 0.45D),
    BLACK_HOLE(5.0D, 1.35D, 4.0F, 24, 20, 0.10D, 0.0D),
    TRAMPOLINE(1.9D, 0.85D, 0.0F, 10, 10, 0.0D, 1.10D),
    LIGHTER(2.4D, 0.95D, 3.0F, 22, 14, 0.0D, 0.0D),
    CAROUSEL(2.8D, 1.2D, 2.0F, 26, 16, 0.35D, 0.25D);

    private final double radius;
    private final double triggerDistance;
    private final float damage;
    private final int cooldownTicks;
    private final int activeTicks;
    private final double force;
    private final double verticalForce;

    AnomalyType(double radius, double triggerDistance, float damage, int cooldownTicks, int activeTicks, double force,
        double verticalForce) {
        this.radius = radius;
        this.triggerDistance = triggerDistance;
        this.damage = damage;
        this.cooldownTicks = cooldownTicks;
        this.activeTicks = activeTicks;
        this.force = force;
        this.verticalForce = verticalForce;
    }

    public double radius() {
        return radius;
    }

    public double triggerDistance() {
        return triggerDistance;
    }

    public float damage() {
        return damage;
    }

    public int cooldownTicks() {
        return cooldownTicks;
    }

    public int activeTicks() {
        return activeTicks;
    }

    public double force() {
        return force;
    }

    public double verticalForce() {
        return verticalForce;
    }
}
