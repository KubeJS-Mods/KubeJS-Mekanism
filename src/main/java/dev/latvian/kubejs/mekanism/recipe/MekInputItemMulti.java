package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.core.RecipeKJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.ingredient.creator.ItemStackIngredientCreator;
import net.minecraft.world.item.crafting.Ingredient;

public class MekInputItemMulti extends InputItem {
	public final ItemStackIngredientCreator.MultiItemStackIngredient stackIngredient;

	public MekInputItemMulti(ItemStackIngredientCreator.MultiItemStackIngredient stackIngredient) {
		super(Ingredient.EMPTY, 0);
		this.stackIngredient = stackIngredient;
	}

	@Override
	public InputItem withCount(int count) {
		if (count <= 0) {
			return InputItem.EMPTY;
		}

		var arr = stackIngredient.getIngredients().toArray(new ItemStackIngredient[0]);

		for (var x = 0; x < arr.length; x++) {
			if (arr[x] instanceof ItemStackIngredientCreator.SingleItemStackIngredient inx && inx.getAmountRaw() != count) {
				for (var i = 0; i < arr.length; i++) {
					if (arr[i] instanceof ItemStackIngredientCreator.SingleItemStackIngredient in && in.getAmountRaw() != count) {
						arr[i] = ItemStackIngredientCreator.INSTANCE.from(in.getInputRaw(), count);
					}
				}

				return new MekInputItemMulti((ItemStackIngredientCreator.MultiItemStackIngredient) ItemStackIngredientCreator.INSTANCE.createMulti(arr));
			}
		}

		return this;
	}

	@Override
	public InputItem copyWithProperties(InputItem original) {
		var arr = stackIngredient.getIngredients().toArray(new ItemStackIngredient[0]);

		for (var i = 0; i < arr.length; i++) {
			if (arr[i] instanceof ItemStackIngredientCreator.SingleItemStackIngredient in && in.getAmountRaw() != original.count) {
				arr[i] = ItemStackIngredientCreator.INSTANCE.from(in.getInputRaw(), original.count);
			}
		}

		return new MekInputItemMulti((ItemStackIngredientCreator.MultiItemStackIngredient) ItemStackIngredientCreator.INSTANCE.createMulti(arr));
	}

	@Override
	public <T> T replaceInput(RecipeKJS recipe, ReplacementMatch match, T previousValue) {
		return previousValue;
	}
}
