interface oreDeposit {
	type: string,
	config: {
		yMin: number,
		yMax: number,
		biomeTag: string
		"blocks": {
			"default": {
				"block": string,
				"chance": number
			}[]
		},
		"samples": {
			"block": string,
			"chance": 1.0
		}[]
	}
}