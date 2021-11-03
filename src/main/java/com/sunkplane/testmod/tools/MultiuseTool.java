package com.sunkplane.testmod.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MultiuseTool extends TieredItem implements Vanishable {
    protected final float speed;
    private final float attackDamageBaseLine;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public MultiuseTool(float attackDamageModifier, float attackSpeedModifier, Tier toolTier, Item.Properties itemProperties) {
        super(toolTier, itemProperties);
        this.speed = toolTier.getSpeed();
        this.attackDamageBaseLine = attackDamageModifier + toolTier.getAttackDamageBonus();
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double)this.attackDamageBaseLine, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)this.speed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public float getDestroySpeed(ItemStack _itemStack, BlockState _blockState) {
        return this.speed;
    }

    public boolean hurtEnemy(ItemStack itemStack, LivingEntity firstEntity, LivingEntity secondEntity) {
        itemStack.hurtAndBreak(2, secondEntity, (consumer) -> {
            consumer.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    };

    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity entity) {
        if (!level.isClientSide && blockState.getDestroySpeed(level, blockPos) != 0.0f) {
            itemStack.hurtAndBreak(1, entity, (consumer) -> {
                consumer.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public float getAttackDamage() { return this.attackDamageBaseLine; }

    @Deprecated
    public boolean isCorrecttoolForDrops(BlockState blockState) {
        if (net.minecraftforge.common.TierSortingRegistry.isTierSorted(getTier())) {
            return net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), blockState);
        }
        int i = this.getTier().getLevel();
        if (i < 3 && blockState.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < 2 && blockState.is(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return i < 1 && blockState.is(BlockTags.NEEDS_STONE_TOOL) ? false : true;
        }
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState blockState) {
        return net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), blockState);
    }
}
