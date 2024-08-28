import { readdir, readFile } from "fs/promises"

const locales = new Map();
for(let locale of await readdir("./locales")) {
    locales.set(locale.replace(".ts", ""), (await import(`../locales/${locale}`)).default)
}

declare global {
    interface String {
        format(...args : string[]): string;
        localize(locale : string): string;
    }
}

String.prototype.format = function(...args : string[]): string {
    return this.replace(/%(\d+)/g, function(match, number) {
        return typeof args[number] != 'undefined' ? args[number] : match;
    });
};

String.prototype.localize = function(locale : string): string {
    return locales.get(locales.has(locale) ? locale : "default")[this]
};

export function tr(key: string, locale: string): string {
    return locales.get(locale)[key]
}

export function getTranslation(locale: string) {
    return locales.get(locale);
}