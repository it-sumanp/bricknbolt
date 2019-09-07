import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IBoltUser } from 'app/shared/model/bolt-user.model';
import { AccountService } from 'app/core';
import { BoltUserService } from './bolt-user.service';

import { Router } from '@angular/router';

@Component({
    selector: 'jhi-bolt-user',
    templateUrl: './bolt-user.component.html'
})
export class BoltUserComponent implements OnInit, OnDestroy {
    boltUsers: IBoltUser[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected boltUserService: BoltUserService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService,
        private router: Router
    ) {}

    loadAll() {
        this.boltUserService
            .query()
            .pipe(
                filter((res: HttpResponse<IBoltUser[]>) => res.ok),
                map((res: HttpResponse<IBoltUser[]>) => res.body)
            )
            .subscribe(
                (res: IBoltUser[]) => {
                    this.boltUsers = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInBoltUsers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IBoltUser) {
        return item.id;
    }

    registerChangeInBoltUsers() {
        this.eventSubscriber = this.eventManager.subscribe('boltUserListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onLinkClick(medium: string, affiliateId: string) {
        this.router.navigate(['/product'], { queryParams: { medium: medium, affiliate: affiliateId } });
    }
}
