package dev.latvian.kubejs.mekanism.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import mekanism.api.JsonConstants;
import mekanism.common.recipe.ingredient.creator.ItemStackIngredientCreator;

public class MekanismRecipeJS extends RecipeJS {
	@Override
	public JsonElement writeInputItem(InputItem in) {
		if (in instanceof MekInputItemMulti mek) {
			return mek.stackIngredient.serialize();
		}

		var json = new JsonObject();
		json.add(JsonConstants.INGREDIENT, in.ingredient.toJson());

		if (in.count > 1) {
			json.addProperty(JsonConstants.AMOUNT, in.count);
		}

		return json;
	}

	@Override
	public InputItem readInputItem(Object from) {
		if (from instanceof InputItem i) {
			return i;
		} else if (from instanceof ItemStackIngredientCreator.SingleItemStackIngredient i) {
			return InputItem.of(i.getInputRaw(), i.getAmountRaw());
		} else if (from instanceof ItemStackIngredientCreator.MultiItemStackIngredient i) {
			return new MekInputItemMulti(i);
		} else if (from instanceof JsonElement j) {
			var s = ItemStackIngredientCreator.INSTANCE.deserialize(j);

			if (s instanceof ItemStackIngredientCreator.SingleItemStackIngredient i) {
				return InputItem.of(i.getInputRaw(), i.getAmountRaw());
			} else if (s instanceof ItemStackIngredientCreator.MultiItemStackIngredient i) {
				return new MekInputItemMulti(i);
			} else {
				return InputItem.EMPTY;
			}
		} else {
			return InputItem.of(from);
		}
	}
}