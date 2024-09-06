package dev.latvian.mods.kubejs.mekanism;

import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import mekanism.api.chemical.attribute.ChemicalAttribute;
import mekanism.api.chemical.attribute.ChemicalAttributes;
import mekanism.common.integration.LazyChemicalProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@ReturnsSelf
public class KubeChemicalBuilder extends BuilderBase<Chemical> {
	@ReturnsSelf
	public static class Default extends KubeChemicalBuilder {
		public Default(ResourceLocation id) {
			super(id);
		}

		public Default texture(ResourceLocation texture) {
			chemicalBuilder = ChemicalBuilder.builder(texture);
			return this;
		}
	}

	public static class Liquid extends KubeChemicalBuilder {
		public Liquid(ResourceLocation id) {
			super(id);
			chemicalBuilder = ChemicalBuilder.builder();
		}
	}

	public static class CleanSlurry extends KubeChemicalBuilder {
		public CleanSlurry(ResourceLocation id) {
			super(id);
			chemicalBuilder = ChemicalBuilder.cleanSlurry();
		}
	}

	public static class DirtySlurry extends KubeChemicalBuilder {
		public DirtySlurry(ResourceLocation id) {
			super(id);
			chemicalBuilder = ChemicalBuilder.dirtySlurry();
		}
	}

	public static class InfuseType extends KubeChemicalBuilder {
		public InfuseType(ResourceLocation id) {
			super(id);
			chemicalBuilder = ChemicalBuilder.infuseType();
		}
	}

	public static class Pigment extends KubeChemicalBuilder {
		public Pigment(ResourceLocation id) {
			super(id);
			chemicalBuilder = ChemicalBuilder.pigment();
		}
	}

	protected ChemicalBuilder chemicalBuilder;

	public KubeChemicalBuilder(ResourceLocation id) {
		super(id);
	}

	private ChemicalBuilder chemicalBuilder() {
		if (chemicalBuilder == null) {
			chemicalBuilder = ChemicalBuilder.builder();
		}

		return chemicalBuilder;
	}

	public KubeChemicalBuilder with(ChemicalAttribute attribute) {
		chemicalBuilder().with(attribute);
		return this;
	}

	public KubeChemicalBuilder fuel(int burnTicks, long energyDensity) {
		if (burnTicks > 0 && energyDensity > 0L) {
			return with(new ChemicalAttributes.Fuel(burnTicks, energyDensity));
		}

		return this;
	}

	public KubeChemicalBuilder heatedCoolant(ResourceLocation chemical, double thermalEnthalpy, double conductivity) {
		if (thermalEnthalpy > 0D && conductivity > 0D) {
			return with(new ChemicalAttributes.HeatedCoolant(new LazyChemicalProvider(chemical), thermalEnthalpy, conductivity));
		}

		return this;
	}

	public KubeChemicalBuilder cooledCoolant(ResourceLocation chemical, double thermalEnthalpy, double conductivity) {
		if (thermalEnthalpy > 0D && conductivity > 0D) {
			return with(new ChemicalAttributes.CooledCoolant(new LazyChemicalProvider(chemical), thermalEnthalpy, conductivity));
		}

		return this;
	}

	public KubeChemicalBuilder radiation(double radioactivity) {
		if (radioactivity > 0D) {
			return with(new ChemicalAttributes.Radiation(radioactivity));
		}

		return this;
	}

	public KubeChemicalBuilder tint(KubeColor tint) {
		chemicalBuilder().tint(tint.kjs$getRGB());
		return this;
	}

	public KubeChemicalBuilder ore(TagKey<Item> oreTag) {
		chemicalBuilder().ore(oreTag);
		return this;
	}

	public KubeChemicalBuilder gaseous() {
		chemicalBuilder().gaseous();
		return this;
	}

	@Override
	public Chemical createObject() {
		return new Chemical(chemicalBuilder());
	}
}
