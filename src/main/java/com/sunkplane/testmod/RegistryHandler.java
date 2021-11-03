package com.sunkplane.testmod;

import com.sunkplane.testmod.tools.ModItemTier;
import com.sunkplane.testmod.tools.MultiuseTool;
import net.minecraft.world.item.*;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TestMod.MODID);

    public static final RegistryObject<MultiuseTool> MULTIUSE_TOOL = ITEMS.register("multiuse_tool", () -> new MultiuseTool(1.0f, 1.0f, ModItemTier.MULTIUSE_TOOL, (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS)));
}
