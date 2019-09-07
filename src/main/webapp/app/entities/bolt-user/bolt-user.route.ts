import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BoltUser } from 'app/shared/model/bolt-user.model';
import { BoltUserService } from './bolt-user.service';
import { BoltUserComponent } from './bolt-user.component';
import { BoltUserDetailComponent } from './bolt-user-detail.component';
import { BoltUserUpdateComponent } from './bolt-user-update.component';
import { BoltUserDeletePopupComponent } from './bolt-user-delete-dialog.component';
import { IBoltUser } from 'app/shared/model/bolt-user.model';

@Injectable({ providedIn: 'root' })
export class BoltUserResolve implements Resolve<IBoltUser> {
    constructor(private service: BoltUserService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBoltUser> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<BoltUser>) => response.ok),
                map((boltUser: HttpResponse<BoltUser>) => boltUser.body)
            );
        }
        return of(new BoltUser());
    }
}

export const boltUserRoute: Routes = [
    {
        path: '',
        component: BoltUserComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bricknboltApp.boltUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: BoltUserDetailComponent,
        resolve: {
            boltUser: BoltUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bricknboltApp.boltUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: BoltUserUpdateComponent,
        resolve: {
            boltUser: BoltUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bricknboltApp.boltUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: BoltUserUpdateComponent,
        resolve: {
            boltUser: BoltUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bricknboltApp.boltUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const boltUserPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: BoltUserDeletePopupComponent,
        resolve: {
            boltUser: BoltUserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'bricknboltApp.boltUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
