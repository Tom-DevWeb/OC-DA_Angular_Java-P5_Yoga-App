import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {RouterTestingModule} from '@angular/router/testing';
import {Router} from '@angular/router';
import {expect} from '@jest/globals';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {RegisterComponent} from "../components/register/register.component";
import {AuthService} from "./auth.service";
import {RegisterRequest} from "../interfaces/registerRequest.interface";

describe('AuthService (Intégration)', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService
  let router: Router;
  let httpTestingController: HttpTestingController

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
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
          { path: 'login', redirectTo: ''}
        ])
      ],
      providers: [AuthService]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    httpTestingController = TestBed.inject(HttpTestingController)

    fixture.detectChanges();
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  //La création de compte
  it('should create an account and redirect to /login', () => {
    const registerRequest: RegisterRequest = {
        email: 'test@example.com',
        firstName: 'Jean',
        lastName: 'DUPONT',
        password: 'password123'
    };

    const navigateSpy = jest.spyOn(router, 'navigate');

    component.form.setValue(registerRequest);
    component.submit();

    // Intercepter l'appel HTTP et retourner la réponse simulée
    const req = httpTestingController.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');

    req.flush(null);

    // Vérifier que la redirection vers '/sessions' a bien eu lieu
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });

});
