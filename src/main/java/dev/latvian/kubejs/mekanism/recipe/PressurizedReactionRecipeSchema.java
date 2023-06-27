package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import mekanism.api.JsonConstants;

public interface PressurizedReactionRecipeSchema {
	RecipeKey<InputItem> ITEM_INPUT = ItemComponents.INPUT.key(JsonConstants.ITEM_INPUT);
	RecipeKey<OutputItem> ITEM_OUTPUT = ItemComponents.OUTPUT.key(JsonConstants.ITEM_OUTPUT).optional(OutputItem.EMPTY);

	RecipeSchema SCHEMA = new RecipeSchema(MekanismRecipeJS.class, MekanismRecipeJS::new, ITEM_INPUT, ITEM_OUTPUT);
}
