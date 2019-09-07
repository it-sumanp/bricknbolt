import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'product',
                loadChildren: './product/product.module#BricknboltProductModule'
            },
            {
                path: 'bolt-user',
                loadChildren: './bolt-user/bolt-user.module#BricknboltBoltUserModule'
            },
            {
                path: 'order',
                loadChildren: './order/order.module#BricknboltOrderModule'
            },
            {
                path: 'bolt-user',
                loadChildren: './bolt-user/bolt-user.module#BricknboltBoltUserModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BricknboltEntityModule {}
