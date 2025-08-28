package dev.latvian.mods.kubejs.mekanism.recipe.component;

import dev.latvian.mods.kubejs.mekanism.MekanismChemicalWrapper;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.CompoundChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.TagChemicalIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;

public class ChemicalIngredientRecipeComponent extends ChemicalLikeRecipeComponent<ChemicalIngredient> {
	public static final RecipeComponentType<ChemicalIngredient> CHEMICAL_INGREDIENT = RecipeComponentType.unit(Mekanism.rl("chemical_ingredient"), t -> new ChemicalIngredientRecipeComponent(t, false));
	public static final RecipeComponentType<ChemicalIngredient> OPTIONAL_CHEMICAL_INGREDIENT = RecipeComponentType.unit(Mekanism.rl("optional_chemical_ingredient"), t -> new ChemicalIngredientRecipeComponent(t, true));

	private ChemicalIngredientRecipeComponent(RecipeComponentType<?> type, boolean allowEmpty) {
		super(type, allowEmpty ? IngredientCreatorAccess.chemical().codec() : IngredientCreatorAccess.chemical().codecNonEmpty(), MekanismChemicalWrapper.CHEMICAL_INGREDIENT_TYPE_INFO);
	}

	@Override
	public boolean matches(RecipeMatchContext cx, ChemicalIngredient value, ReplacementMatchInfo match) {
		return match.match() instanceof ChemicalIngredient m && !value.isEmpty() && value.getChemicalHolders().stream().anyMatch(m::test);
	}

	@Override
	public boolean allowEmpty() {
		return codec == IngredientCreatorAccess.chemical().codec();
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
