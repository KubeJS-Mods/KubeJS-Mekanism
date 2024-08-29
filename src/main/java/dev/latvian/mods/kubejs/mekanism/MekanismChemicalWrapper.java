package dev.latvian.mods.kubejs.mekanism;

import com.mojang.brigadier.StringReader;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.latvian.mods.rhino.util.HideFromJS;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.EmptyChemicalIngredient;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;
import org.codehaus.plexus.util.cli.CommandLineException;

public interface MekanismChemicalWrapper {
	TypeInfo CHEMICAL_TYPE_INFO = TypeInfo.of(Chemical.class);
	TypeInfo CHEMICAL_STACK_TYPE_INFO = TypeInfo.of(ChemicalStack.class);
	TypeInfo CHEMICAL_INGREDIENT_TYPE_INFO = TypeInfo.of(ChemicalIngredient.class);
	TypeInfo CHEMICAL_STACK_INGREDIENT_TYPE_INFO = TypeInfo.of(ChemicalStackIngredient.class);

	static Chemical of(Object from) {
		return switch (from) {
			case null -> MekanismAPI.EMPTY_CHEMICAL;
			case Chemical c -> c;
			case ChemicalStack c -> c.getChemical();
			default -> {
				var str = from.toString();

				if (str.isEmpty() || str.equals("mekanism:empty")) {
					yield MekanismAPI.EMPTY_CHEMICAL;
				} else {
					yield MekanismAPI.CHEMICAL_REGISTRY.get(ResourceLocation.parse(str));
				}
			}
		};
	}

	static ChemicalStack stack(Chemical chemical, long amount) {
		return new ChemicalStack(chemical, amount);
	}

	private static long readAmount(StringReader reader) throws CommandLineException {
		// long amount = FluidWrapper.readFluidAmount(reader);
		return 0L;
	}

	@HideFromJS
	static ChemicalStack wrapStack(Object from) {
		return switch (from) {
			case null -> null;
			case ChemicalStack c -> c;
			case Chemical c -> new ChemicalStack(c, FluidType.BUCKET_VOLUME);
			default -> {
				var str = from.toString();

				if (str.isEmpty() || str.equals("mekanism:empty")) {
					yield ChemicalStack.EMPTY;
				} else {
					yield new ChemicalStack(of(from), FluidType.BUCKET_VOLUME);
				}
			}
		};
	}

	@HideFromJS
	static ChemicalIngredient wrapIngredient(Object from) {
		return EmptyChemicalIngredient.INSTANCE;
	}

	static ChemicalStackIngredient ingredientStack(ChemicalIngredient ingredient, long amount) {
		return new ChemicalStackIngredient(ingredient, amount);
	}

	@HideFromJS
	static ChemicalStackIngredient wrapStackIngredient(Object from) {
		return new ChemicalStackIngredient(EmptyChemicalIngredient.INSTANCE, 1);
	}
}
