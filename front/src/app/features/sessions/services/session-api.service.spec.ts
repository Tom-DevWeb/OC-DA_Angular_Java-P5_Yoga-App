import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Session} from "../interfaces/session.interface";
import { SessionApiService } from './session-api.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";

describe('SessionsService', () => {
  let sessionApiService: SessionApiService;
  let httpTestingController: HttpTestingController

  const mockSessions: Session[] = [
    { id: 1, name: 'Yoga Basics', description: 'Beginner yoga session', date: new Date(), teacher_id: 1, users: [] },
    { id: 2, name: 'Advanced Yoga', description: 'Advanced level session', date: new Date(), teacher_id: 2, users: [] },
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers: [SessionApiService]
    });
    sessionApiService = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController)
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(sessionApiService).toBeTruthy();
  });

  //Affichage de la liste des sessions
  it('should display the list of sessions', () => {
    sessionApiService.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);

  });

  //Les informations de la session sont correctement affichées
  it('should correctly display session information', () => {
    const sessionId = '1';
    const mockSession: Session = {
      id: 1,
      name: 'Yoga Basics',
      description: 'Beginner yoga session',
      date: new Date(),
      teacher_id: 1,
      users: [],
    };

    sessionApiService.detail(sessionId).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  //La session est crée
  it('should create a session', () => {
    const newSession: Session = {
      name: 'Power Yoga',
      description: 'High-intensity yoga session',
      date: new Date(),
      teacher_id: 3,
      users: [],
    };

    sessionApiService.create(newSession).subscribe((session) => {
      expect(session).toEqual({ id: 3, ...newSession });
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush({ id: 3, ...newSession });
  });

  //La session est correctement supprimé
  it('should correctly delete a session', () => {
    const sessionId = '1';

    sessionApiService.delete(sessionId).subscribe((response) => {
      expect(response).toBeNull();
    });

    const req = httpTestingController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  //La session est modifiée
  it('should modify a session', () => {
    const sessionId = '2';
    const updatedSession: Session = {
      id: 2,
      name: 'Advanced Yoga',
      description: 'Updated session description',
      date: new Date(),
      teacher_id: 2,
      users: [],
    };

    sessionApiService.update(sessionId, updatedSession).subscribe((session) => {
      expect(session).toEqual(updatedSession);
    });

    const req = httpTestingController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('PUT');
    req.flush(updatedSession);
  });
});
