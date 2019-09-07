export interface IProduct {
    id?: number;
    name?: string;
    price?: number;
}

export class Product implements IProduct {
    constructor(public id?: number, public name?: string, public price?: number) {}
}
