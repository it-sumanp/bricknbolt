import { Component, OnInit, OnDestroy, AfterViewInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Moment } from 'moment';

import { IProduct } from 'app/shared/model/product.model';
import { AccountService } from 'app/core';
import { ProductService } from './product.service';
import { OrderService } from '../order/order.service';

import { ActivatedRoute } from '@angular/router';

import { IBoltUser, BoltUser } from 'app/shared/model/bolt-user.model';

import { IOrder } from 'app/shared/model/order.model';
import moment = require('moment');

@Component({
    selector: 'jhi-product',
    templateUrl: './product.component.html'
})
export class ProductComponent implements OnInit, OnDestroy, AfterViewInit {
    products: IProduct[];
    currentAccount: any;
    eventSubscriber: Subscription;
    medium: string;
    affiliate: string;
    buyer: IBoltUser = new BoltUser();

    constructor(
        protected productService: ProductService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService,
        protected orderService: OrderService,
        private activatedRoute: ActivatedRoute
    ) {}

    loadAll() {
        this.productService
            .query()
            .pipe(
                filter((res: HttpResponse<IProduct[]>) => res.ok),
                map((res: HttpResponse<IProduct[]>) => res.body)
            )
            .subscribe(
                (res: IProduct[]) => {
                    this.products = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInProducts();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IProduct) {
        return item.id;
    }

    registerChangeInProducts() {
        this.eventSubscriber = this.eventManager.subscribe('productListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    ngAfterViewInit() {
        this.activatedRoute.queryParams.subscribe(query => {
            this.medium = query['medium'];
            this.affiliate = query['affiliate'];
        });
    }

    buyProduct(productId) {
        this.products.forEach(product => {
            if (product.id === productId) {
                product['open'] = true;
            }
        });
    }

    checkout(productId) {
        if (!this.buyer.name) {
            alert('Please Enter Your Name');
        }

        if (!this.buyer.phone) {
            alert('Please Enter Your Phone Number');
        }

        const order: IOrder = {
            orderDate: moment(),
            medium: this.medium,
            buyer: this.buyer,
            referrer: {
                phone: this.affiliate
            },
            item: {
                id: productId
            },
            price: this.products.find(product => product.id === productId).price
        };
        this.orderService.create(order).subscribe(res => alert('success'), err => alert('Error'));
    }
}
