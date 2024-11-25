import {ComponentFixture, TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';
import {TeacherService} from './teacher.service';
import {DetailComponent} from "../features/sessions/components/detail/detail.component";
import {FormComponent} from "../features/sessions/components/form/form.component";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Session} from "../features/sessions/interfaces/session.interface";
import {Teacher} from "../interfaces/teacher.interface";
import {RouterTestingModule} from "@angular/router/testing";
import {ActivatedRoute, Router} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatSelectModule} from "@angular/material/select";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {SessionService} from "./session.service";

describe('TeacherService', () => {
  let service: TeacherService;
  let detailComponent: DetailComponent
  let detailFixture: ComponentFixture<DetailComponent>
  let formComponent: FormComponent
  let formFixture: ComponentFixture<FormComponent>
  let httpTestingController: HttpTestingController
  let router: Router

  const mockSession: Session = {
    id: 1,
    name: 'Yoga Basics',
    description: 'Beginner yoga session',
    date: new Date(),
    teacher_id: 1,
    users: [],
  }

  const mockTeacher: Teacher = {
    id: 1,
    lastName: 'Doe',
    firstName: 'John',
    createdAt: new Date('2023-01-01T00:00:00Z'),
    updatedAt: new Date('2023-01-02T00:00:00Z'),
  }

  const mockSessionService = {
    sessionInformation: {
      id: 1,
      admin: true,
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetailComponent, FormComponent],
      imports:[
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([
          { path: 'sessions', redirectTo: ''}
        ]),
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: new Map([['id', '1']])}}},
        TeacherService
      ]
    }).compileComponents()

    formFixture = TestBed.createComponent(FormComponent)
    formComponent = formFixture.componentInstance
    detailFixture = TestBed.createComponent(DetailComponent)
    detailComponent = detailFixture.componentInstance
    service = TestBed.inject(TeacherService);
    router = TestBed.inject(Router)
    httpTestingController = TestBed.inject(HttpTestingController)

    formFixture.detectChanges()

  })

  afterEach(() => {
    httpTestingController.verify()
  })

  describe('FormComponent', () => {
    //Création d'une session et appel de api/teacher
    it('should  create a session and call api/teacher', () => {

      const mockSession: Session = {
        id: 1,
        name: 'Yoga Basics',
        description: 'Beginner yoga session',
        date: new Date(),
        teacher_id: 1,
        users: [],
      };

      const navigateSpy = jest.spyOn(router, 'navigate');

      formComponent.onUpdate = false
      formFixture.detectChanges()

      formComponent.sessionForm?.setValue({
        name: mockSession.name,
        date: mockSession.date,
        teacher_id: mockSession.teacher_id,
        description: mockSession.description,
      });
      formComponent.submit()

      const reqSession = httpTestingController.expectOne('api/session')
      expect(reqSession.request.method).toBe('POST')

      reqSession.flush(mockSession)

      expect(navigateSpy).toHaveBeenCalledWith(['sessions'])

      const reqTeacher = httpTestingController.expectOne('api/teacher')
      expect(reqTeacher.request.method).toBe('GET')

      reqTeacher.flush(mockTeacher)
    });
  });

  describe('DetailComponent', () => {
    //Appel de fetch session
    it('should fetch session', () => {
      detailFixture.detectChanges()

      const reqSession =
        httpTestingController.expectOne(`api/session/1`)
      expect(reqSession.request.method).toBe('GET')
      reqSession.flush(mockSession)

      const reqTeacher =
        httpTestingController.expectOne(`api/teacher/${mockSession.teacher_id}`)
      expect(reqTeacher.request.method).toBe('GET')
      reqTeacher.flush(mockTeacher)

      httpTestingController.match(() => true);

      expect(detailComponent.session).toEqual(mockSession)
      expect(detailComponent.teacher).toEqual(mockTeacher)

    });

    it('should navigate back on calling back()', () => {
      const spyBack = jest.spyOn(window.history, 'back');
      detailComponent.back();
      expect(spyBack).toHaveBeenCalled();

      httpTestingController.match(() => true);
    });

    it('should delete session and navigate to sessions', () => {
      const navigateSpy = jest.spyOn(router, 'navigate');

      detailComponent.delete();

      const req = httpTestingController.expectOne(`api/session/1`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);

      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);

      httpTestingController.match(() => true);
    });

    it('should participate in session and fetch session again', () => {
      detailComponent.participate();

      const reqParticipate = httpTestingController.expectOne(`api/session/1/participate/1`);
      expect(reqParticipate.request.method).toBe('POST');
      reqParticipate.flush(null);

      const reqSession = httpTestingController.expectOne(`api/session/1`);
      expect(reqSession.request.method).toBe('GET');
      reqSession.flush(mockSession);

      const reqTeacher = httpTestingController.expectOne(`api/teacher/${mockSession.teacher_id}`);
      expect(reqTeacher.request.method).toBe('GET');
      reqTeacher.flush(mockTeacher);

      expect(detailComponent.session).toEqual(mockSession);
      expect(detailComponent.teacher).toEqual(mockTeacher);

      httpTestingController.match(() => true);
    });

    it('should unParticipate in session and fetch session again', () => {
      detailComponent.unParticipate();

      const reqUnParticipate = httpTestingController.expectOne(`api/session/1/participate/1`);
      expect(reqUnParticipate.request.method).toBe('DELETE');
      reqUnParticipate.flush(null);

      const reqSession = httpTestingController.expectOne(`api/session/1`);
      expect(reqSession.request.method).toBe('GET');
      reqSession.flush(mockSession);

      const reqTeacher = httpTestingController.expectOne(`api/teacher/${mockSession.teacher_id}`);
      expect(reqTeacher.request.method).toBe('GET');
      reqTeacher.flush(mockTeacher);

      expect(detailComponent.session).toEqual(mockSession);
      expect(detailComponent.teacher).toEqual(mockTeacher);

      httpTestingController.match(() => true);
    });
  });
});
