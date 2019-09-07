import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBoltUser } from 'app/shared/model/bolt-user.model';

type EntityResponseType = HttpResponse<IBoltUser>;
type EntityArrayResponseType = HttpResponse<IBoltUser[]>;

@Injectable({ providedIn: 'root' })
export class BoltUserService {
    public resourceUrl = SERVER_API_URL + 'api/bolt-users';

    constructor(protected http: HttpClient) {}

    create(boltUser: IBoltUser): Observable<EntityResponseType> {
        return this.http.post<IBoltUser>(this.resourceUrl, boltUser, { observe: 'response' });
    }

    update(boltUser: IBoltUser): Observable<EntityResponseType> {
        return this.http.put<IBoltUser>(this.resourceUrl, boltUser, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBoltUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBoltUser[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
