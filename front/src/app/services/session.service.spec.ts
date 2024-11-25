import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {RouterTestingModule} from '@angular/router/testing';
import {Router} from '@angular/router';
import {LoginComponent} from "../features/auth/components/login/login.component";
import {AuthService} from "../features/auth/services/auth.service";
import {SessionService} from 'src/app/services/session.service';
import {SessionInformation} from 'src/app/interfaces/sessionInformation.interface';
import {LoginRequest} from "../features/auth/interfaces/loginRequest.interface";
import {expect} from '@jest/globals';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {AppComponent} from "../app.component";
import {MatToolbarModule} from "@angular/material/toolbar";

describe('SessionService (Intégration)', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let appComponent: AppComponent
  let appFixture: ComponentFixture<AppComponent>
  let sessionService: SessionService;
  let router: Router;
  let httpTestingController: HttpTestingController

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent, AppComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        HttpClientTestingModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        MatToolbarModule,
        RouterTestingModule.withRoutes([
          { path: 'sessions', redirectTo: ''}
        ])
      ],
      providers: [AuthService, SessionService]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    appFixture = TestBed.createComponent(AppComponent)
    appComponent = appFixture.componentInstance
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

  //La déconnexion de l'utilisateur
  it('should log out the user', () => {
    const navigateSpy = jest.spyOn(router, 'navigate');
    const sessionLogOutSpy = jest.spyOn(sessionService, 'logOut');

    appComponent.logout();

    // Vérifier que le service de session est appelé pour déconnexion
    expect(sessionLogOutSpy).toHaveBeenCalled();

    // Vérifier que les données de session sont effacées
    expect(sessionService.sessionInformation).toBeUndefined();
    expect(sessionService.isLogged).toBe(false);

    // Vérifier que l'utilisateur est redirigé vers la page de login
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });
});
