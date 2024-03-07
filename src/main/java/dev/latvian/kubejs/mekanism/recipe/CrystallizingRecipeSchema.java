package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import mekanism.api.JsonConstants;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;

public interface CrystallizingRecipeSchema {
	enum CHEMICALS {
		GAS,
		PIGMENT,
		INFUSE_TYPE,
		SLURRY
	}
	RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key(JsonConstants.OUTPUT);
	RecipeKey<ChemicalStack<?>> INPUT = MekComponents.BOXED_CHEMICAL_OUTPUT.key(JsonConstants.INPUT);
	RecipeKey<CrystallizingRecipeSchema.CHEMICALS> CHEMICAL_TYPE = new EnumComponent<>(CrystallizingRecipeSchema.CHEMICALS.class).key(JsonConstants.CHEMICAL_TYPE);

	RecipeSchema SCHEMA = new RecipeSchema(MekanismRecipeJS.class, MekanismRecipeJS::new, CHEMICAL_TYPE, OUTPUT, INPUT);
}