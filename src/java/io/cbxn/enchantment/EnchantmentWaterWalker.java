package java.io.cbxn.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by Apex on 22/02/2016.
 */
public class EnchantmentWaterWalker extends Enchantment {
    public EnchantmentWaterWalker(int p_i45762_1_, int p_i45762_3_)
    {
        super(p_i45762_1_, p_i45762_3_, EnumEnchantmentType.armor_feet);
        this.setName("waterWalker");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return enchantmentLevel * 10;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }

    @Override
    public int getMaxLevel()
    {
        return 3;
    }

    @Override
    public String getName()
    {
        return "Depth Strider";
    }
}
