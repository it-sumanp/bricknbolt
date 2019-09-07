import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBoltUser } from 'app/shared/model/bolt-user.model';
import { BoltUserService } from './bolt-user.service';

@Component({
    selector: 'jhi-bolt-user-delete-dialog',
    templateUrl: './bolt-user-delete-dialog.component.html'
})
export class BoltUserDeleteDialogComponent {
    boltUser: IBoltUser;

    constructor(protected boltUserService: BoltUserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.boltUserService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'boltUserListModification',
                content: 'Deleted an boltUser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bolt-user-delete-popup',
    template: ''
})
export class BoltUserDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ boltUser }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BoltUserDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.boltUser = boltUser;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/bolt-user', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/bolt-user', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
