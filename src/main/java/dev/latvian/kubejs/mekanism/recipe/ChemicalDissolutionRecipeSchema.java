package dev.latvian.kubejs.mekanism.recipe;


import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import mekanism.api.JsonConstants;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;

public interface ChemicalDissolutionRecipeSchema {
	RecipeKey<GasStack> OUTPUT = MekComponents.GAS_OUTPUT.key(JsonConstants.OUTPUT);
	RecipeKey<ChemicalStackIngredient.GasStackIngredient> CHEMICAL_INPUT = MekComponents.GAS_INPUT.key(JsonConstants.CHEMICAL_INPUT);
	RecipeKey<InputItem> ITEM_INPUT = ItemComponents.INPUT.key(JsonConstants.ITEM_INPUT);

	RecipeSchema SCHEMA = new RecipeSchema(MekanismRecipeJS.class, MekanismRecipeJS::new, OUTPUT, CHEMICAL_INPUT, ITEM_INPUT);
}
