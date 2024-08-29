package dev.latvian.mods.kubejs.mekanism;

import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.SimpleRecipeComponent;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;

public interface ChemicalRecipeComponents {
	RecipeComponent<Chemical> CHEMICAL = new SimpleRecipeComponent<>("mekanism:chemical", Chemical.CODEC, MekanismChemicalWrapper.CHEMICAL_TYPE_INFO);
	RecipeComponent<ChemicalStack> CHEMICAL_STACK = new SimpleRecipeComponent<>("mekanism:chemical_stack", ChemicalStack.OPTIONAL_CODEC, MekanismChemicalWrapper.CHEMICAL_STACK_TYPE_INFO);
	RecipeComponent<ChemicalIngredient> CHEMICAL_INGREDIENT = new SimpleRecipeComponent<>("mekanism:chemical_ingredient", IngredientCreatorAccess.chemical().codec(), MekanismChemicalWrapper.CHEMICAL_INGREDIENT_TYPE_INFO);
	RecipeComponent<ChemicalStackIngredient> CHEMICAL_STACK_INGREDIENT = new SimpleRecipeComponent<>("mekanism:chemical_stack_ingredient", ChemicalStackIngredient.CODEC, MekanismChemicalWrapper.CHEMICAL_STACK_INGREDIENT_TYPE_INFO);
}
