import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { DonationService } from 'app/entities/donation/donation.service';
import { IDonation, Donation } from 'app/shared/model/donation.model';
import { IsActiveStatus } from 'app/shared/model/enumerations/is-active-status.model';

describe('Service Tests', () => {
  describe('Donation Service', () => {
    let injector: TestBed;
    let service: DonationService;
    let httpMock: HttpTestingController;
    let elemDefault: IDonation;
    let expectedResult: IDonation | IDonation[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(DonationService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Donation(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
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

      it('should create a Donation', () => {
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

        service.create(new Donation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Donation', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            url: 'BBBBBB',
            imageUrl: 'BBBBBB',
            paymentUrl: 'BBBBBB',
            bankAccountNumber: 'BBBBBB',
            bankAccountName: 'BBBBBB',
            bankName: 'BBBBBB',
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

      it('should return a list of Donation', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            url: 'BBBBBB',
            imageUrl: 'BBBBBB',
            paymentUrl: 'BBBBBB',
            bankAccountNumber: 'BBBBBB',
            bankAccountName: 'BBBBBB',
            bankName: 'BBBBBB',
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

      it('should delete a Donation', () => {
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
