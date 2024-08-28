import { readdir, readFile, writeFile } from "fs/promises";
import { getTranslation } from "./common/stringUtils";

const locales = ["en_us", "ru_ru"]
const modLocales = {} as {
	[key: string]: {}
};
const depositTypes = await readdir("../../src/main/resources/data/geolosys/deposits");

for (const locale of locales) {
	modLocales[locale] = JSON.parse(await readFile(`../../src/main/resources/assets/geolosys/lang/${locale}.json`, "utf-8"))
}
for (const depositType of depositTypes) {
	const deposits = await readdir("../../src/main/resources/data/geolosys/deposits/" + depositType);
	for (const deposit of deposits) {
		const depositData = JSON.parse(await readFile(`../../src/main/resources/data/geolosys/deposits/${depositType}/${deposit}`, "utf-8")) as oreDeposit;
		const resourceKey = `${depositType}.${deposit}`
		console.log(resourceKey, depositData.type)
		for(const locale of locales) {
			const translation = getTranslation(locale);
			const hasSample = depositData.config.samples[0].block != null;
			const block = depositData.config.blocks.default[0].block;
			const sample = depositData.config.samples[0].block;
			const blockName = modLocales[locale][`block.${depositData.config.blocks.default[0].block.replace(":", ".")}`];
			const sampleName = hasSample ? modLocales[locale][`block.${depositData.config.samples[0].block.replace(":", ".")}`] : undefined;
			writeFile(`../../src/main/resources/assets/geolosys/patchouli_books/field_manual/${locale}/entries/03_resources/${resourceKey}`, JSON.stringify({
				"name": blockName || translation.exclusionBlockNames[block],
				"icon": block,
				"category": "geolosys:03_resources",
				"pages": [
					{
						"type": "spotlight",
						"title": blockName,
						"item": block,
						"link_recipe": false,
						"text": depositData.type == "geolosys:deposit_top_layer" 
							? translation["patchouli.field_manual.resource.page1.top_layer.text"](
								translation.depositTypes[depositData.type],
								translation.biomesTags[depositData.config.biomeTag],
							)
							: translation["patchouli.field_manual.resource.page1.other.text"](
								translation.depositTypes[depositData.type],
								translation.biomesTags[depositData.config.biomeTag],
								depositData.config.yMin - 62,
								depositData.config.yMax - 62,
								depositData.config.yMin,
								depositData.config.yMax
							)
					},
					{
						"type": hasSample ? "spotlight" : "text",
						"title": hasSample ? sampleName : translation["patchouli.field_manual.resource.page2.title"],
						"item": hasSample ? sample : undefined,
						"text": depositData.type == "geolosys:deposit_top_layer" 
							? translation["patchouli.field_manual.resource.page2.top_layer.text"](
								translation.depositTypes[depositData.type],
								translation.biomesTags[depositData.config.biomeTag]
							)
							: translation["patchouli.field_manual.resource.page2.other.text"](
								translation.depositTypes[depositData.type],
								translation.biomesTags[depositData.config.biomeTag],
								depositData.config.yMin - 62,
								depositData.config.yMax - 62,
								depositData.config.yMin,
								depositData.config.yMax
							)
					}
				]
			}, null, 4))
		}
	}
}