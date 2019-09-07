import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoltUser } from 'app/shared/model/bolt-user.model';

@Component({
    selector: 'jhi-bolt-user-detail',
    templateUrl: './bolt-user-detail.component.html'
})
export class BoltUserDetailComponent implements OnInit {
    boltUser: IBoltUser;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ boltUser }) => {
            this.boltUser = boltUser;
        });
    }

    previousState() {
        window.history.back();
    }
}
