/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { BricknboltTestModule } from '../../../test.module';
import { BoltUserUpdateComponent } from 'app/entities/bolt-user/bolt-user-update.component';
import { BoltUserService } from 'app/entities/bolt-user/bolt-user.service';
import { BoltUser } from 'app/shared/model/bolt-user.model';

describe('Component Tests', () => {
    describe('BoltUser Management Update Component', () => {
        let comp: BoltUserUpdateComponent;
        let fixture: ComponentFixture<BoltUserUpdateComponent>;
        let service: BoltUserService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BricknboltTestModule],
                declarations: [BoltUserUpdateComponent]
            })
                .overrideTemplate(BoltUserUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BoltUserUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BoltUserService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new BoltUser(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.boltUser = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new BoltUser();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.boltUser = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
