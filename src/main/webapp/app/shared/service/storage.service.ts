import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class StorageService {
  public resourceUrl = SERVER_API_URL + 'api/storages';

  private filename = new BehaviorSubject<string>('');
  sharedFilename = this.filename.asObservable();

  constructor(protected http: HttpClient) {}

  upload(image: File): void {
    let generatedFileName = '';
    const name = encodeURI(image.name);
    this.http.get(`${this.resourceUrl}/url/${name}`, { responseType: 'text' }).subscribe(body => {
      generatedFileName = body.substring(body.lastIndexOf('/') + 1, body.indexOf('?'));
      const httpHeaders = new HttpHeaders({ 'Content-Type': 'image/' + image.type });
      this.http.put(body, image, { headers: httpHeaders }).subscribe(() => {
        this.filename.next(generatedFileName);
      });
    });
  }
}
