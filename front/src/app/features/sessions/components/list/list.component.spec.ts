import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';
import {ListComponent} from './list.component';
import {By} from "@angular/platform-browser";
import {of} from "rxjs";
import {Session} from "../../interfaces/session.interface";
import {RouterTestingModule} from "@angular/router/testing";

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'Yoga Class',
      description: 'Relaxing yoga session',
      date: new Date('2024-11-25'),
      teacher_id: 101,
      users: [1, 2, 3],
      createdAt: new Date('2024-11-01'),
      updatedAt: new Date('2024-11-15'),
    },
    {
      id: 2,
      name: 'Cooking Class',
      description: 'Learn to cook delicious meals',
      date: new Date('2024-11-26'),
      teacher_id: 102,
      users: [4, 5],
      createdAt: new Date('2024-11-05'),
      updatedAt: new Date('2024-11-16'),
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        RouterTestingModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;

    component.sessions$ = of(mockSessions);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //L'apparition des boutons Create et Detail si l'utilisateur connecté est un admin
  it('should display the Create button if the logged-in is an admin', () => {

    const createButton =
      fixture.debugElement.query(By.css('button[routerLink="create"]'))
    expect(createButton.nativeElement.textContent).toContain("Create")
  });

  it('should display the Edit button for each session if the logged-in user is an admin', () => {

    const editButtons = fixture.debugElement.queryAll(
      By.css('#editButton')
    );

    expect(editButtons.length).toBe(2); // Deux sessions -> deux boutons "Edit"
    expect(editButtons[0].nativeElement.textContent).toContain('Edit');
    expect(editButtons[1].nativeElement.textContent).toContain('Edit');
  });
});
