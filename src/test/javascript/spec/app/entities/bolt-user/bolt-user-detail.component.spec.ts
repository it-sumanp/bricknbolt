/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BricknboltTestModule } from '../../../test.module';
import { BoltUserDetailComponent } from 'app/entities/bolt-user/bolt-user-detail.component';
import { BoltUser } from 'app/shared/model/bolt-user.model';

describe('Component Tests', () => {
    describe('BoltUser Management Detail Component', () => {
        let comp: BoltUserDetailComponent;
        let fixture: ComponentFixture<BoltUserDetailComponent>;
        const route = ({ data: of({ boltUser: new BoltUser(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BricknboltTestModule],
                declarations: [BoltUserDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BoltUserDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BoltUserDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.boltUser).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
