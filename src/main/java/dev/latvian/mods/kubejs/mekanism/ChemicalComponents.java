package dev.latvian.mods.kubejs.mekanism;

import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.SimpleRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.rhino.Context;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.CompoundChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.TagChemicalIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;

public interface ChemicalComponents {
	private static boolean isChemicalLike(Object obj) {
		return obj instanceof Chemical || obj instanceof ChemicalStack || obj instanceof ChemicalIngredient || obj instanceof ChemicalStackIngredient;
	}

	RecipeComponent<Chemical> CHEMICAL = new SimpleRecipeComponent<>("mekanism:chemical", Chemical.CODEC, MekanismChemicalWrapper.CHEMICAL_TYPE_INFO) {
		@Override
		public boolean hasPriority(Context cx, KubeRecipe recipe, Object from) {
			return isChemicalLike(from);
		}

		@Override
		public boolean matches(Context cx, KubeRecipe recipe, Chemical value, ReplacementMatchInfo match) {
			return match.match() instanceof ChemicalIngredient m && !value.isEmptyType() && m.test(value);
		}

		@Override
		public boolean isEmpty(Chemical value) {
			return value.isEmptyType();
		}

		@Override
		public void buildUniqueId(UniqueIdBuilder builder, Chemical value) {
			builder.append(value.getRegistryName());
		}
	};

	RecipeComponent<ChemicalStack> CHEMICAL_STACK = new SimpleRecipeComponent<>("mekanism:chemical_stack", ChemicalStack.OPTIONAL_CODEC, MekanismChemicalWrapper.CHEMICAL_STACK_TYPE_INFO) {
		@Override
		public boolean hasPriority(Context cx, KubeRecipe recipe, Object from) {
			return isChemicalLike(from);
		}

		@Override
		public boolean matches(Context cx, KubeRecipe recipe, ChemicalStack value, ReplacementMatchInfo match) {
			return match.match() instanceof ChemicalIngredient m && !value.isEmpty() && m.test(value.getChemical());
		}

		@Override
		public boolean isEmpty(ChemicalStack value) {
			return value.isEmpty();
		}

		@Override
		public void buildUniqueId(UniqueIdBuilder builder, ChemicalStack value) {
			builder.append(value.getTypeRegistryName());
		}
	};

	RecipeComponent<ChemicalIngredient> CHEMICAL_INGREDIENT = new SimpleRecipeComponent<>("mekanism:chemical_ingredient", IngredientCreatorAccess.chemical().codec(), MekanismChemicalWrapper.CHEMICAL_INGREDIENT_TYPE_INFO) {
		@Override
		public boolean hasPriority(Context cx, KubeRecipe recipe, Object from) {
			return isChemicalLike(from);
		}

		@Override
		public boolean matches(Context cx, KubeRecipe recipe, ChemicalIngredient value, ReplacementMatchInfo match) {
			return match.match() instanceof ChemicalIngredient m && !value.isEmpty() && value.getChemicals().stream().anyMatch(m);
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
				var list = value.getChemicals();

				if (!list.isEmpty()) {
					builder.append(list.getFirst().getRegistryName());
				}
			}
		}
	};

	RecipeComponent<ChemicalStackIngredient> CHEMICAL_STACK_INGREDIENT = new SimpleRecipeComponent<>("mekanism:chemical_stack_ingredient", ChemicalStackIngredient.CODEC, MekanismChemicalWrapper.CHEMICAL_STACK_INGREDIENT_TYPE_INFO) {
		@Override
		public boolean hasPriority(Context cx, KubeRecipe recipe, Object from) {
			return isChemicalLike(from);
		}

		@Override
		public boolean matches(Context cx, KubeRecipe recipe, ChemicalStackIngredient value, ReplacementMatchInfo match) {
			return match.match() instanceof ChemicalIngredient m && !value.ingredient().isEmpty() && value.ingredient().getChemicals().stream().anyMatch(m);
		}

		@Override
		public boolean isEmpty(ChemicalStackIngredient value) {
			return value.ingredient().isEmpty();
		}

		@Override
		public void buildUniqueId(UniqueIdBuilder builder, ChemicalStackIngredient value) {
			CHEMICAL_INGREDIENT.buildUniqueId(builder, value.ingredient());
		}
	};
}
