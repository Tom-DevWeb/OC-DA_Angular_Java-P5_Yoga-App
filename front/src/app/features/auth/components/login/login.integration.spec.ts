import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {RouterTestingModule} from '@angular/router/testing';
import {Router} from '@angular/router';
import {LoginComponent} from './login.component';
import {AuthService} from '../../services/auth.service';
import {SessionService} from 'src/app/services/session.service';
import {SessionInformation} from 'src/app/interfaces/sessionInformation.interface';
import {LoginRequest} from "../../interfaces/loginRequest.interface";
import {expect} from '@jest/globals';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";

describe('LoginComponent (Integration Test)', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let sessionService: SessionService;
  let router: Router;
  let httpTestingController: HttpTestingController

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        HttpClientTestingModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        RouterTestingModule.withRoutes([
          { path: 'sessions', redirectTo: ''}
        ])
      ],
      providers: [AuthService, SessionService]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    httpTestingController = TestBed.inject(HttpTestingController)

    fixture.detectChanges();
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should login and redirect to /sessions on success', () => {
    const mockSessionInfo: SessionInformation = {
      token: 'mockToken',
      type: 'admin',
      id: 1,
      username: 'testuser',
      firstName: 'John',
      lastName: 'Doe',
      admin: true
    };

    const loginRequest: LoginRequest =
      { email: 'test@example.com', password: 'password123' };

    const navigateSpy = jest.spyOn(router, 'navigate');

    component.form.setValue(loginRequest);
    component.submit();

    // Intercepter l'appel HTTP et retourner la réponse simulée
    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');

    req.flush(mockSessionInfo);

    // Vérifier que le service SessionService reçoit bien les informations de session
    expect(sessionService.sessionInformation).toEqual(mockSessionInfo);

    // Vérifier que la redirection vers '/sessions' a bien eu lieu
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should show an error message if login fails', () => {
    const loginRequest: LoginRequest = {
      email: 'wrong@example.com',
      password: 'wrongPassword'
    };

    const navigateSpy = jest.spyOn(router, 'navigate');

    component.form.setValue(loginRequest);
    component.submit();

    // Simuler une erreur HTTP 401 avec HttpTestingController
    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush({ error: 'Unauthorized' }, { status: 401, statusText: 'Unauthorized' });

    // Vérifier que l'état d'erreur est activé après l'échec
    expect(component.onError).toBe(true);

    // Vérifier qu'aucune redirection n'a eu lieu
    expect(navigateSpy).not.toHaveBeenCalled();
  });

});
