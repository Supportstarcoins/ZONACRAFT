package net.minecraftforge.event.entity.living;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class LivingHurtEvent {
    public EntityLivingBase entityLiving;
    public DamageSource source;
    public float ammount;
}
