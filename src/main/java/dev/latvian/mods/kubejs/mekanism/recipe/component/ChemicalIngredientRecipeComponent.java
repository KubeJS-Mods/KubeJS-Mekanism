package dev.latvian.mods.kubejs.mekanism.recipe.component;

import dev.latvian.mods.kubejs.mekanism.MekanismChemicalWrapper;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.rhino.Context;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.CompoundChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.TagChemicalIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;

public class ChemicalIngredientRecipeComponent extends ChemicalLikeRecipeComponent<ChemicalIngredient> {
	public static final RecipeComponentType<ChemicalIngredient> CHEMICAL_INGREDIENT = RecipeComponentType.unit(Mekanism.rl("chemical_ingredient"), ChemicalIngredientRecipeComponent::new);

	private ChemicalIngredientRecipeComponent(RecipeComponentType<?> type) {
		super(type, IngredientCreatorAccess.chemical().codec(), MekanismChemicalWrapper.CHEMICAL_INGREDIENT_TYPE_INFO);
	}

	@Override
	public boolean matches(Context cx, KubeRecipe recipe, ChemicalIngredient value, ReplacementMatchInfo match) {
		return match.match() instanceof ChemicalIngredient m && !value.isEmpty() && value.getChemicalHolders().stream().anyMatch(m::test);
	}

	@Override
	public boolean isEmpty(ChemicalIngredient value) {
		return value.isEmpty();
	}

	@Override
	public void buildUniqueId(UniqueIdBuilder builder, ChemicalIngredient value) {
		if (value instanceof CompoundChemicalIngredient c) {
			boolean first = true;

			for (var in : c.children()) {
				if (first) {
					first = false;
				} else {
					builder.appendSeparator();
				}

				buildUniqueId(builder, in);
			}
		} else if (value instanceof TagChemicalIngredient tag) {
			builder.append(tag.tag().location());
		} else {
			var list = value.getChemicalHolders();

			if (!list.isEmpty()) {
				builder.append(list.getFirst().getKey());
			}
		}
	}
}
