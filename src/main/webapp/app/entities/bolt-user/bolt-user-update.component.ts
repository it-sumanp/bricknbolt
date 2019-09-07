import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IBoltUser } from 'app/shared/model/bolt-user.model';
import { BoltUserService } from './bolt-user.service';

@Component({
    selector: 'jhi-bolt-user-update',
    templateUrl: './bolt-user-update.component.html'
})
export class BoltUserUpdateComponent implements OnInit {
    boltUser: IBoltUser;
    isSaving: boolean;

    constructor(protected boltUserService: BoltUserService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ boltUser }) => {
            this.boltUser = boltUser;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.boltUser.id !== undefined) {
            this.subscribeToSaveResponse(this.boltUserService.update(this.boltUser));
        } else {
            this.subscribeToSaveResponse(this.boltUserService.create(this.boltUser));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoltUser>>) {
        result.subscribe((res: HttpResponse<IBoltUser>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
