package me.ht9.end.event.events;

import me.ht9.end.event.Event;
import net.minecraft.src.Entity;
import net.minecraft.src.ModelBase;

public final class RenderEntityEvent extends Event
{
    private final ModelBase modelBase;
    private final Entity entity;
    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float netHeadYaw;
    private float headPitch;
    private float scale;
    private final Stage stage;

    public RenderEntityEvent(ModelBase modelBase, Entity entityPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Stage stage)
    {
        this.modelBase = modelBase;
        this.entity = entityPlayer;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scale = scale;
        this.stage = stage;
    }

    public ModelBase getModelBase()
    {
        return this.modelBase;
    }

    public Entity getEntity()
    {
        return this.entity;
    }

    public float getLimbSwing()
    {
        return this.limbSwing;
    }

    public float getLimbSwingAmount()
    {
        return this.limbSwingAmount;
    }

    public float getAgeInTicks()
    {
        return this.ageInTicks;
    }

    public float getNetHeadYaw()
    {
        return this.netHeadYaw;
    }

    public float getHeadPitch()
    {
        return this.headPitch;
    }

    public float getScale()
    {
        return this.scale;
    }

    public Stage getStage()
    {
        return this.stage;
    }

    public void setLimbSwing(float limbSwing)
    {
        this.limbSwing = limbSwing;
    }

    public void setLimbSwingAmount(float limbSwingAmount)
    {
        this.limbSwingAmount = limbSwingAmount;
    }

    public void setAgeInTicks(float ageInTicks)
    {
        this.ageInTicks = ageInTicks;
    }

    public void setNetHeadYaw(float netHeadYaw)
    {
        this.netHeadYaw = netHeadYaw;
    }

    public void setHeadPitch(float headPitch)
    {
        this.headPitch = headPitch;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public enum Stage
    {
        PRE,
        POST
    }
}