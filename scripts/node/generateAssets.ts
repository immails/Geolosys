import { mkdir, writeFile } from "fs/promises";

try {
	await mkdir("./generated/models/block", {recursive: true})
	await mkdir("./generated/models/item", {recursive: true})
	await mkdir("./generated/blockstates/", {recursive: true})
} catch(e) {}

const args = process.argv;
const oreTexture = args[2];
const baseTexture = args[3];

console.log("ore texture is", oreTexture);
console.log("base texture is", baseTexture);
if (!baseTexture) {
	console.log("1st arg is ore texture, 2nd arg is base texture")
	process.exit()
}

const modid = oreTexture.replace(/:.*/, "")
const fileName = `${oreTexture.replace(/.*(:|\/)/, "").replace("ore", "sample")}`

writeFile(`./generated/models/block/${fileName}.json`, JSON.stringify({
	"parent": "geolosys:block/ore_sample",
	"textures": {
		"particle": baseTexture,
		"ore": oreTexture,
		"stone": baseTexture
	}
}))

writeFile(`./generated/models/item/${fileName}.json`, JSON.stringify({
	"parent": `${modid}:block/${fileName}`
}))

writeFile(`./generated/blockstates/${fileName}.json`, JSON.stringify({
	"variants": {
		"": [
				{
					"model": `${modid}:block/${fileName}`
				}
			]
		}
	}
))