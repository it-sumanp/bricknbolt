import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BricknboltSharedModule } from 'app/shared';
import {
    BoltUserComponent,
    BoltUserDetailComponent,
    BoltUserUpdateComponent,
    BoltUserDeletePopupComponent,
    BoltUserDeleteDialogComponent,
    boltUserRoute,
    boltUserPopupRoute
} from './';

const ENTITY_STATES = [...boltUserRoute, ...boltUserPopupRoute];

@NgModule({
    imports: [BricknboltSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BoltUserComponent,
        BoltUserDetailComponent,
        BoltUserUpdateComponent,
        BoltUserDeleteDialogComponent,
        BoltUserDeletePopupComponent
    ],
    entryComponents: [BoltUserComponent, BoltUserUpdateComponent, BoltUserDeleteDialogComponent, BoltUserDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BricknboltBoltUserModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
