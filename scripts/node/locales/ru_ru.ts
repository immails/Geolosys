export default {
	biomesTags: {
		"#minecraft:is_mountain": "Горах",
		"#minecraft:is_overworld": "Верхнем мире",
		"#geolosys:is_sandy": "Песчанных биомах",
		"#forge:is_plains": "Равнинах",
		"#minecraft:is_nether": "Незере",
		"#forge:is_dry/overworld": "Сухих биомах",
		"#geolosys:is_marshy": "Болотистых биомах",
		"#forge:is_hot/overworld": "Горячих биомах Верхнего мира"
	},
	depositTypes: {
		"geolosys:deposit_dense": "Плотное",
		"geolosys:deposit_dike": "Дайковое",
		"geolosys:deposit_sparse": "Редкое",
		"geolosys:deposit_layer": "Слоистое",
		"geolosys:deposit_top_layer": "Поверхностное Слоистое"
	},
	exclusionBlockNames: {
		"minecraft:andesite": "Андезит",
		"minecraft:diorite": "Диорит",
		"minecraft:granite": "Гранит"
	},
	"patchouli.field_manual.resource.page1.other.text": (depositTypeLocalized: string, locationLocalized: string, relativeMinY: number, relativeMaxY: number, realMinY: number, realMaxY: number) => 
		`$(l)Локация: $()В ${locationLocalized}$(br)$(l)Мин. глубина: $()${relativeMinY > 0 ? "+" + relativeMinY : relativeMinY}$(br)$(l)Макс. глубина: $()${relativeMaxY > 0 ? "+" + relativeMaxY : relativeMaxY}$(br)$(l)Тип месторождения: $()${depositTypeLocalized}`,
	"patchouli.field_manual.resource.page1.top_layer.text": (depositTypeLocalized: string, locationLocalized: string) => 
		`$(l)Локация: $()В ${locationLocalized}$(br)$(l)Тип месторождения: $()${depositTypeLocalized}`,
	"patchouli.field_manual.resource.page2.other.text": (depositTypeLocalized: string, locationLocalized: string, relativeMinY: number, relativeMaxY: number, realMinY: number, realMaxY: number) => 
		`Это $(l)${depositTypeLocalized}$() месторождение может быть найдено между $(l)${Math.abs(relativeMinY)}м. ${relativeMinY < 0 ? "ниже" : "выше"}$() и $(l)${Math.abs(relativeMaxY)}м. ${relativeMaxY < 0 ? "ниже" : "выше"}$() уровня моря, в $(l)${locationLocalized}.$()`,
	"patchouli.field_manual.resource.page2.top_layer.text": (depositTypeLocalized: string, locationLocalized: string) => 
		`Это $(l)${depositTypeLocalized}$() месторождение может быть найдено в $(l)${locationLocalized}.$()`,

	"patchouli.field_manual.resource.page2.title": "Описание",
}