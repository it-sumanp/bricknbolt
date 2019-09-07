import { Moment } from 'moment';
import { IBoltUser } from 'app/shared/model/bolt-user.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IOrder {
    id?: number;
    price?: number;
    orderDate?: Moment;
    medium?: string;
    buyer?: IBoltUser;
    referrer?: IBoltUser;
    item?: IProduct;
}

export class Order implements IOrder {
    constructor(
        public id?: number,
        public price?: number,
        public orderDate?: Moment,
        public medium?: string,
        public buyer?: IBoltUser,
        public referrer?: IBoltUser,
        public item?: IProduct
    ) {}
}
