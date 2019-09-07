/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BricknboltTestModule } from '../../../test.module';
import { BoltUserComponent } from 'app/entities/bolt-user/bolt-user.component';
import { BoltUserService } from 'app/entities/bolt-user/bolt-user.service';
import { BoltUser } from 'app/shared/model/bolt-user.model';

describe('Component Tests', () => {
    describe('BoltUser Management Component', () => {
        let comp: BoltUserComponent;
        let fixture: ComponentFixture<BoltUserComponent>;
        let service: BoltUserService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BricknboltTestModule],
                declarations: [BoltUserComponent],
                providers: []
            })
                .overrideTemplate(BoltUserComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BoltUserComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BoltUserService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new BoltUser(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.boltUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
