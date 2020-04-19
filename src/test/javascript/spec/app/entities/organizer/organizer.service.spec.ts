import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { OrganizerService } from 'app/entities/organizer/organizer.service';
import { IOrganizer, Organizer } from 'app/shared/model/organizer.model';
import { IsActiveStatus } from 'app/shared/model/enumerations/is-active-status.model';

describe('Service Tests', () => {
  describe('Organizer Service', () => {
    let injector: TestBed;
    let service: OrganizerService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrganizer;
    let expectedResult: IOrganizer | IOrganizer[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(OrganizerService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Organizer(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        'AAAAAAA',
        currentDate,
        IsActiveStatus.ACTIVE
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            lastUpdatedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Organizer', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            lastUpdatedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastUpdatedAt: currentDate
          },
          returnedFromService
        );

        service.create(new Organizer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Organizer', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            url: 'BBBBBB',
            email: 'BBBBBB',
            bankAccountNumber: 'BBBBBB',
            bankAccountName: 'BBBBBB',
            bankName: 'BBBBBB',
            mdr: 1,
            sharing: 1,
            lastUpdatedBy: 'BBBBBB',
            lastUpdatedAt: currentDate.format(DATE_TIME_FORMAT),
            status: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastUpdatedAt: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Organizer', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            url: 'BBBBBB',
            email: 'BBBBBB',
            bankAccountNumber: 'BBBBBB',
            bankAccountName: 'BBBBBB',
            bankName: 'BBBBBB',
            mdr: 1,
            sharing: 1,
            lastUpdatedBy: 'BBBBBB',
            lastUpdatedAt: currentDate.format(DATE_TIME_FORMAT),
            status: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastUpdatedAt: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Organizer', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
