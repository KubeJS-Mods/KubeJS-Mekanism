package dev.latvian.mods.kubejs.mekanism.recipe.component;

import dev.latvian.mods.kubejs.mekanism.MekanismChemicalWrapper;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.common.Mekanism;

public class ChemicalStackRecipeComponent extends ChemicalLikeRecipeComponent<ChemicalStack> {
	public static final RecipeComponentType<ChemicalStack> CHEMICAL_STACK = RecipeComponentType.unit(Mekanism.rl("chemical_stack"), t -> new ChemicalStackRecipeComponent(t, false));
	public static final RecipeComponentType<ChemicalStack> OPTIONAL_CHEMICAL_STACK = RecipeComponentType.unit(Mekanism.rl("optional_chemical_stack"), t -> new ChemicalStackRecipeComponent(t, true));

	private ChemicalStackRecipeComponent(RecipeComponentType<?> type, boolean allowEmpty) {
		super(type, allowEmpty ? ChemicalStack.OPTIONAL_CODEC : ChemicalStack.CODEC, MekanismChemicalWrapper.CHEMICAL_STACK_TYPE_INFO);
	}

	@Override
	public boolean matches(RecipeMatchContext cx, ChemicalStack value, ReplacementMatchInfo match) {
		return match.match() instanceof ChemicalIngredient m && !value.isEmpty() && m.test(value.getChemicalHolder());
	}

	@Override
	public boolean allowEmpty() {
		return codec == ChemicalStack.OPTIONAL_CODEC;
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
