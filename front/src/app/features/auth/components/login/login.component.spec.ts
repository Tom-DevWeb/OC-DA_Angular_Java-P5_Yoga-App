import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {expect} from '@jest/globals';
import {LoginComponent} from './login.component';
import {LoginRequest} from "../../interfaces/loginRequest.interface";
import {AuthService} from "../../services/auth.service";
import {throwError} from "rxjs";
import {By} from "@angular/platform-browser";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: jest.Mocked<AuthService>

  beforeEach(async () => {
    authServiceMock = {
      login: jest.fn()
    } as any
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [{ provide: AuthService, useValue: authServiceMock}],
      imports: [
        BrowserAnimationsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //La gestion des erreurs en cas de mauvais login/password
  it('should handle errors when login/password are incorrect', () => {
    const loginRequest: LoginRequest =
      { email: 'test@example.com', password: 'wrongPassword' };

    authServiceMock.login.mockReturnValue(
      throwError(() => new Error('Unauthorized'))
    )

    component.form.setValue(loginRequest)
    component.submit()

    expect(component.onError).toBeTruthy()
  });

  //L'affichage d'erreur en l'absence d'un champ obligatoire
  it('should display an error message when a required field is missing', () => {
    component.form.setValue({
      email: '',
      password: ''
    })

    fixture.detectChanges()

    const submitButton =
      fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement

    expect(submitButton.disabled).toBeTruthy()
  });
});
