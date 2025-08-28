package dev.latvian.mods.kubejs.mekanism.recipe.component;

import dev.latvian.mods.kubejs.mekanism.MekanismChemicalWrapper;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.common.Mekanism;

public class ChemicalStackIngredientRecipeComponent extends ChemicalLikeRecipeComponent<ChemicalStackIngredient> {
	public static final RecipeComponentType<ChemicalStackIngredient> CHEMICAL_STACK_INGREDIENT = RecipeComponentType.unit(Mekanism.rl("chemical_stack_ingredient"), ChemicalStackIngredientRecipeComponent::new);

	private ChemicalStackIngredientRecipeComponent(RecipeComponentType<?> type) {
		super(type, ChemicalStackIngredient.CODEC, MekanismChemicalWrapper.CHEMICAL_STACK_INGREDIENT_TYPE_INFO);
	}

	@Override
	public boolean matches(RecipeMatchContext cx, ChemicalStackIngredient value, ReplacementMatchInfo match) {
		return match.match() instanceof ChemicalIngredient m && !value.ingredient().isEmpty() && value.ingredient().getChemicalHolders().stream().anyMatch(m::test);
	}

	@Override
	public boolean isEmpty(ChemicalStackIngredient value) {
		return value.ingredient().isEmpty();
	}

	@Override
	public void buildUniqueId(UniqueIdBuilder builder, ChemicalStackIngredient value) {
		ChemicalIngredientRecipeComponent.CHEMICAL_INGREDIENT.instance().buildUniqueId(builder, value.ingredient());
	}
}
