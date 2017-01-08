package java.io.cbxn.core;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import java.io.cbxn.enchantment.EnchantmentWaterWalker;

import java.util.Collections;

/**
 * Created by Apex on 22/02/2016.
 */
public class ApexCore extends DummyModContainer
{
    private static Minecraft mc;

    public ApexCore()
    {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "depthstrider";
        meta.name = "Depth Strider";
        meta.description = "Adds the Depth Strider enchantment for 1.7.10";
        meta.version = "1.7.10-1.0";
        meta.authorList = Collections.singletonList("cbxn");
        meta.url = "http://cbxn.io/";
        meta.updateUrl = "http://cbxn.io/";
        System.out.println("APEXCORE IS STARTING UP");
        waterWalker = new EnchantmentWaterWalker(8, 2);
        mc = Minecraft.getMinecraft();
    }

    private static Enchantment waterWalker;


    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }


    public static float getVMove() {
        if (mc.thePlayer == null || mc.thePlayer.getLastActiveItems() == null) return 0;

        float verticalMotion = 0.02F;
        float enchantmentLevel = EnchantmentHelper.getMaxEnchantmentLevel(waterWalker.effectId, mc.thePlayer.getLastActiveItems());

        if (enchantmentLevel > 3.0F) {
            enchantmentLevel = 3.0F;
        }

        if (!mc.thePlayer.onGround) {
            enchantmentLevel *= 0.5F;
        }

        if (enchantmentLevel > 0.0F) {
            verticalMotion += (mc.thePlayer.getAIMoveSpeed() * 1.0F - verticalMotion) * enchantmentLevel / 3.0F;
        }

        return verticalMotion;
    }

    public static double getHMove() {
        if (mc.thePlayer == null || mc.thePlayer.getLastActiveItems() == null) return 0;

        float horizontalMotion = 0.800000011920929F;
        float enchantmentLevel = EnchantmentHelper.getMaxEnchantmentLevel(waterWalker.effectId, mc.thePlayer.getLastActiveItems());

        if (enchantmentLevel > 3.0F) {
            enchantmentLevel = 3.0F;
        }

        if (!mc.thePlayer.onGround) {
            enchantmentLevel *= 0.5F;
        }

        if (enchantmentLevel > 0.0F) {
            horizontalMotion += (0.54600006F - horizontalMotion) * enchantmentLevel / 3.0F;
        }

        return horizontalMotion;
    }
}