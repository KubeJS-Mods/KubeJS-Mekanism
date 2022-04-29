// registers a basic custom gas,
// as well as a radioactive gas with a reddish tint
onEvent('mekanism.gas.registry', event => {
	event.create("test")
	event.create("danger").color(0xCC5500).radioactivity(10)
})

// registers a dirty and a clean slurry, as well
// as a slurry with a "custom" texture
// note: recipes are not automatically generated!
onEvent('mekanism.slurry.registry', event => {
	event.create('test').texture('mekanism:slurry/dirty').color(0x42387F)

	event.create("dirty_kubium_slurry", "dirty").color(0xFCB95B).ore('forge:ores/kubium')
	event.create("clean_kubium_slurry", "clean").color(0xFCB95B).ore('forge:ores/kubium')
})

// for more detailed explanations, see the javadoc of the builder classes
// (that's right, we've got javadoc for once!)
