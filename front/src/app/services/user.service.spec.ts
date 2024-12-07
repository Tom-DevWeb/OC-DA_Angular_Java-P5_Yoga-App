import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';
import {UserService} from './user.service';
import {MeComponent} from "../components/me/me.component";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {SessionService} from "./session.service";
import {By} from "@angular/platform-browser";
import {MatSnackBar, MatSnackBarModule} from "@angular/material/snack-bar";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";

describe('UserService (Intégration)', () => {
  let service: UserService;
  let sessionService: SessionService
  let component: MeComponent
  let fixture: ComponentFixture<MeComponent>
  let httpTestingController: HttpTestingController
  let matSnackBar: MatSnackBar;

  beforeEach( async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports:[
        HttpClientModule,
        HttpClientTestingModule,
        MatSnackBarModule,
        MatCardModule,
        MatIconModule,
        RouterTestingModule.withRoutes([
          { path: '', redirectTo: '', pathMatch: 'full'}
        ])
      ],
      providers: [UserService, SessionService]
    }).compileComponents()
    service = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService)
    fixture = TestBed.createComponent(MeComponent)
    component = fixture.componentInstance
    httpTestingController = TestBed.inject(HttpTestingController)
    matSnackBar = TestBed.inject(MatSnackBar)

    sessionService.sessionInformation = {
      id: 1,
      token: 'mockToken',
      type: 'admin',
      username: 'testuser',
      firstName: 'John',
      lastName: 'Doe',
      admin: false,
    };

    component.user = {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: 'fakepassword',
      admin: false,
      createdAt: new Date(),
      updatedAt: new Date(),
    };
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  //La suppression de son compte par l'utilisateur
  it('should delete the user account and log out', () => {
    const snackBarSpy = jest.spyOn(matSnackBar, 'open')

    fixture.detectChanges();

    const deleteButton = fixture.debugElement.query(By.css('#deleteButton'));
    expect(deleteButton).toBeTruthy();

    deleteButton.nativeElement.click();

    const reqDelete = httpTestingController.expectOne({
      method: 'DELETE',
      url: 'api/user/1',
    })

    expect(reqDelete.request.method).toBe('DELETE')

    reqDelete.flush({}, { status: 200, statusText: 'OK' })

    const reqGet = httpTestingController.expectOne({ method: 'GET', url: 'api/user/1'})
    expect(reqGet.request.method).toBe('GET');
    reqGet.flush({});

    expect(snackBarSpy).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
  });
});
