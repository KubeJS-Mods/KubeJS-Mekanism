package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import mekanism.api.JsonConstants;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.tags.MekanismTags;

public interface MetallurgicInfusingRecipeSchema {
	RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key(JsonConstants.OUTPUT);
	RecipeKey<ItemStackIngredient> INPUT = MekComponents.INPUT_ITEM.key(JsonConstants.INPUT);
	RecipeKey<ChemicalStackIngredient.InfusionStackIngredient> CHEMICAL_INPUT = MekComponents.INFUSE_TYPE_INPUT.key(JsonConstants.CHEMICAL_INPUT).optional(IngredientCreatorAccess.infusion().from(MekanismTags.InfuseTypes.REDSTONE, 10));

	RecipeSchema SCHEMA = new RecipeSchema(MekanismRecipeJS.class, MekanismRecipeJS::new, OUTPUT, INPUT, CHEMICAL_INPUT);
}