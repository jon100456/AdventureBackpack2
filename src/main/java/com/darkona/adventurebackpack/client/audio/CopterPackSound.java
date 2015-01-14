package com.darkona.adventurebackpack.client.audio;

import com.darkona.adventurebackpack.item.ItemCopterPack;
import com.darkona.adventurebackpack.proxy.ClientProxy;
import com.darkona.adventurebackpack.reference.ModInfo;
import com.darkona.adventurebackpack.util.LogHelper;
import com.darkona.adventurebackpack.util.Wearing;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

/**
 * Created on 16/10/2014
 *
 * @author Darkona
 */
public class CopterPackSound extends MovingSound
{

    public EntityPlayer thePlayer;
    private boolean repeat;
    private UUID playerID;

    protected float pitch;

    public CopterPackSound()
    {
        super(new ResourceLocation(ModInfo.MOD_ID, "helicopter"));
        this.repeat = true;
        this.volume = 0.8f;
        this.pitch = 1.0F;
    }


    public CopterPackSound(EntityPlayer player)
    {
        super(new ResourceLocation(ModInfo.MOD_ID, "helicopter"));
        volume = 0.8f;
        pitch = 1.0F;
        thePlayer = player;
        playerID = thePlayer.getUniqueID();
        repeat = true;
        LogHelper.info("Sound Created");
    }

    public EntityPlayer getThePlayer()
    {
        return thePlayer;
    }

    public void setThePlayer(EntityPlayer player){
        thePlayer = player;
        playerID = thePlayer.getUniqueID();
    }

    public void setRepeat(boolean newRepeat){
        LogHelper.info("Setting sound repeat");
        repeat = newRepeat;
    }
    public void setDonePlaying()
    {
        this.donePlaying = true;
    }
    long messagetime = 0;
    @Override
    public void update()
    {
        ItemStack copter = Wearing.getWearingCopter(thePlayer);
        byte status = 0;
       if(thePlayer == null || thePlayer.worldObj == null)
        {
            LogHelper.info("Stopped playing copter sound");
            setDonePlaying();
            ClientProxy.soundPoolCopters.remove(playerID);
        }
        if (copter != null&& copter.hasTagCompound() && copter.getTagCompound().hasKey("status"))
        {
            status = copter.getTagCompound().getByte("status");
            if (status == ItemCopterPack.OFF_MODE)
            {
                volume = 0.0f;
            }else{
                volume = 0.8F;
                if(status == ItemCopterPack.HOVER_MODE)
                {
                    pitch = (thePlayer.motionY == 0) ? 1.0f : (thePlayer.motionY > 0) ? 1.2f : 0.8f;
                }
                else
                {
                    pitch = (thePlayer.onGround || thePlayer.motionY == 0) ?  0.8f : (thePlayer.isSneaking()) ? 0.8f : (thePlayer.motionY > 0) ? 1.2f : 1.0F;
                }
            }
        }else{
            volume = 0.0F;
        }
        xPosF = (float)thePlayer.posX;
        yPosF = (float)thePlayer.posY;
        zPosF = (float)thePlayer.posZ;
    }

    @Override
    public boolean canRepeat()
    {
        return this.repeat;
    }

    @Override
    public float getVolume()
    {
        return this.volume;
    }

    @Override
    public float getPitch()
    {
        return this.pitch;
    }

    @Override
    public AttenuationType getAttenuationType()
    {
        return AttenuationType.LINEAR;
    }
}