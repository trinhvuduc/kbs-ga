export const storeData = (key, value) => sessionStorage.setItem(key, JSON.stringify(value));

export const extractData = (key) => JSON.parse(sessionStorage.getItem(key));

export const clearStore = () => sessionStorage.clear();