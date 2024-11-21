import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {RouterTestingModule,} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from '../../../../services/session.service';

import {DetailComponent} from './detail.component';
import {By} from "@angular/platform-browser";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;

  const sessionServiceMock = {
    sessionInformation: {
      token: 'fake-token',
      type: 'Bearer',
      id: 1,
      username: 'testUser',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatButtonModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [{ provide: SessionService, useValue: sessionServiceMock }],
    })
      .compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    // Simulez une session active
    component.session = {
      name: 'Yoga Session',
      users: [],
      date: new Date(),
      description: 'Relaxing yoga session',
      createdAt: new Date(),
      updatedAt: new Date(),
      teacher_id: 1,
    };

    component.teacher = {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //Le bouton Delete apparaît si l'utilisateur connecté est un admin
  it('should display the Delete button if the logged-in user is an admin', () => {

    sessionServiceMock.sessionInformation!.admin = true
    component.isAdmin = sessionServiceMock.sessionInformation!.admin;
    fixture.detectChanges()

    expect(component.isAdmin).toBeTruthy();

    const deleteButton = fixture.debugElement.query(By.css('#deleteButton'))
    expect(deleteButton).toBeTruthy()
    expect(deleteButton.nativeElement.textContent).toContain('Delete')

  });

  // Vérifie que les informations du teacher s'affichent correctement
  it('should display teacher information if teacher is defined', () => {
    const teacherInfo = fixture.debugElement.query(By.css('mat-card-subtitle div'));
    expect(teacherInfo).toBeTruthy();
    expect(teacherInfo.nativeElement.textContent).toContain('John');
    expect(teacherInfo.nativeElement.textContent).toContain('DOE');
  });

  // Vérifie que le sous-titre du teacher n'apparaît pas si teacher est null
  it('should not display teacher information if teacher is not defined', () => {
    component.teacher = undefined;
    fixture.detectChanges();

    const teacherInfo = fixture.debugElement.query(By.css('mat-card-subtitle div'));
    expect(teacherInfo).toBeFalsy();
  });
});

