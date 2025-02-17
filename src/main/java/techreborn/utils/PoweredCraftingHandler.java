/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import reborncore.api.events.ItemCraftCallback;
import reborncore.common.powerSystem.RcEnergyItem;
import techreborn.TechReborn;

import java.util.stream.IntStream;

public final class PoweredCraftingHandler implements ItemCraftCallback {

	private PoweredCraftingHandler() {
	}

	public static void setup() {
		ItemCraftCallback.EVENT.register(new PoweredCraftingHandler());
	}

	@Override
	public void onCraft(ItemStack stack, RecipeInputInventory craftingInventory, PlayerEntity playerEntity) {
		if (stack.getItem() instanceof RcEnergyItem energyItem) {
			long totalEnergy = IntStream.range(0, craftingInventory.size())
					.mapToObj(craftingInventory::getStack)
					.filter(s -> !s.isEmpty())
					.mapToLong(s -> {
						if (s.getItem() instanceof RcEnergyItem inputItem) {
							return inputItem.getStoredEnergy(s);
						} else {
							return 0;
						}
					})
					.sum();

			energyItem.setStoredEnergy(stack, Math.min(totalEnergy, energyItem.getEnergyCapacity(stack)));
		}

		if (!Registries.ITEM.getId(stack.getItem()).getNamespace().equalsIgnoreCase(TechReborn.MOD_ID)) {
			return;
		}

		ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);

		boolean didEnchant = false;

		for (int i = 0; i < craftingInventory.size(); i++) {
			ItemStack ingredient = craftingInventory.getStack(i);
			if (ingredient.isEmpty()) {
				continue;
			}
			ItemEnchantmentsComponent existing = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);

			for (RegistryEntry<Enchantment> enchantment : existing.getEnchantments()) {
				builder.add(enchantment, existing.getLevel(enchantment));
				didEnchant = true;
			}
		}

		if (didEnchant) {
			EnchantmentHelper.set(stack, builder.build());
		}
	}

}
