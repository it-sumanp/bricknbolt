export interface IBoltUser {
    id?: number;
    name?: string;
    phone?: string;
}

export class BoltUser implements IBoltUser {
    constructor(public id?: number, public name?: string, public phone?: string) {}
}
