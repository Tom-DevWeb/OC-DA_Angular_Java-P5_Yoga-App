import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import {User} from "../../interfaces/user.interface";
import {By} from "@angular/platform-browser";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //Affichage des informations de l'utilisateur
  it("should display the user's information", () => {
    const user: User = {
      id: 1,
      email: 'test@example.fr',
      lastName: "Jean",
      firstName: "DUPONT",
      admin: false,
      password: "password123",
      createdAt: new Date("2024-11-15 11:05:38"),
      updatedAt: new Date("2024-11-15 11:05:38")
    }

    component.user = user
    fixture.detectChanges()

    const nameElement =
      fixture.debugElement.query(By.css('p:nth-child(1)')).nativeElement
    expect(nameElement.textContent).toContain('Name: DUPONT JEAN')

    const emailElement = fixture.debugElement.query(By.css('p:nth-child(2)')).nativeElement;
    expect(emailElement.textContent).toContain('Email: test@example.fr');

    const adminElement = fixture.debugElement.query(By.css('.my2 p'));
    expect(adminElement).toBeTruthy();
    expect(adminElement.nativeElement.textContent).toContain('Delete my account:');

    const createdAtElement = fixture.debugElement.query(By.css('.p2 p:nth-child(1)')).nativeElement;
    expect(createdAtElement.textContent).toContain('Create at:  November 15, 2024');

    const updatedAtElement = fixture.debugElement.query(By.css('.p2 p:nth-child(2)')).nativeElement;
    expect(updatedAtElement.textContent).toContain('Last update:  November 15, 2024');
  });

  //Affichage des informations de l'utilisateur admin
  it("should display the user's information", () => {
    const user: User = {
      id: 1,
      email: 'test@example.fr',
      lastName: "Jean",
      firstName: "DUPONT",
      admin: true,
      password: "password123",
      createdAt: new Date("2024-11-15 11:05:38"),
      updatedAt: new Date("2024-11-15 11:05:38")
    }

    component.user = user
    fixture.detectChanges()

    const nameElement =
      fixture.debugElement.query(By.css('p:nth-child(3)')).nativeElement
    expect(nameElement.textContent).toContain('You are admin')

    const emailElement = fixture.debugElement.query(By.css('p:nth-child(2)')).nativeElement;
    expect(emailElement.textContent).toContain('Email: test@example.fr');
  })
});
