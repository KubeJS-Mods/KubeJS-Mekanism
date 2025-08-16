package dev.latvian.mods.kubejs.mekanism.recipe.component;

import dev.latvian.mods.kubejs.mekanism.MekanismChemicalWrapper;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.rhino.Context;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.common.Mekanism;

public class ChemicalStackRecipeComponent extends ChemicalLikeRecipeComponent<ChemicalStack> {
	public static final RecipeComponentType<ChemicalStack> CHEMICAL_STACK = RecipeComponentType.unit(Mekanism.rl("chemical_stack"), ChemicalStackRecipeComponent::new);

	private ChemicalStackRecipeComponent(RecipeComponentType<?> type) {
		super(type, ChemicalStack.OPTIONAL_CODEC, MekanismChemicalWrapper.CHEMICAL_STACK_TYPE_INFO);
	}

	@Override
	public boolean matches(Context cx, KubeRecipe recipe, ChemicalStack value, ReplacementMatchInfo match) {
		return match.match() instanceof ChemicalIngredient m && !value.isEmpty() && m.test(value.getChemicalHolder());
	}

	@Override
	public boolean isEmpty(ChemicalStack value) {
		return value.isEmpty();
	}

	@Override
	public void buildUniqueId(UniqueIdBuilder builder, ChemicalStack value) {
		builder.append(value.getChemicalHolder().getKey());
	}
}
