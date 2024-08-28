export default {
	biomesTags: {
		"#minecraft:is_mountain": "Mountains",
		"#minecraft:is_overworld": "Overworld",
		"#geolosys:is_sandy": "Sandy biomes",
		"#forge:is_plains": "Plains",
		"#minecraft:is_nether": "Nether",
		"#forge:is_dry/overworld": "Dry biomes",
		"#geolosys:is_marshy": "Marshy biomes",
		"#forge:is_hot/overworld": "Hot overworld biomes"
	},
	depositTypes: {
		"geolosys:deposit_dense": "Dense",
		"geolosys:deposit_dike": "Dike",
		"geolosys:deposit_sparse": "Sparse",
		"geolosys:deposit_layer": "Layered",
		"geolosys:deposit_top_layer": "Top Layer"
	},
	exclusionBlockNames: {
		"minecraft:andesite": "Andesite",
		"minecraft:diorite": "Diorite",
		"minecraft:granite": "Granite"
	},
	"patchouli.field_manual.resource.page1.other.text": (depositTypeLocalized: string, locationLocalized: string, relativeMinY: number, relativeMaxY: number, realMinY: number, realMaxY: number) => 
		`$(l)Location: $()${locationLocalized}$(br)$(l)Min. depth: $()${relativeMinY > 0 ? "+" + relativeMinY : relativeMinY}$(br)$(l)Max. depth: $()${relativeMaxY > 0 ? "+" + relativeMaxY : relativeMaxY}$(br)$(l)Deposit type: $()${depositTypeLocalized}`,
	"patchouli.field_manual.resource.page1.top_layer.text": (depositTypeLocalized: string, locationLocalized: string) => 
		`$(l)Location: $()${locationLocalized}$(br)$(l)Deposit type: $()${depositTypeLocalized}`,
	"patchouli.field_manual.resource.page2.other.text": (depositTypeLocalized: string, locationLocalized: string, relativeMinY: number, relativeMaxY: number, realMinY: number, realMaxY: number) => 
		`This $(l)${depositTypeLocalized}$() deposit can be found between $(l)${Math.abs(relativeMinY)}m. ${relativeMinY < 0 ? "below" : "above"}$() and $(l)${Math.abs(relativeMaxY)}m. ${relativeMaxY < 0 ? "below" : "above"}$() sea level, in $(l)${locationLocalized}.$()`,
	"patchouli.field_manual.resource.page2.top_layer.text": (depositTypeLocalized: string, locationLocalized: string) => 
		`This $(l)${depositTypeLocalized}$() deposit can be found in $(l)${locationLocalized}.$()`,
	"patchouli.field_manual.resource.page2.title": "Description",
}