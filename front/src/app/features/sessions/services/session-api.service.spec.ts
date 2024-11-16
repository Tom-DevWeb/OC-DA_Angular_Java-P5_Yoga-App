import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';

describe('SessionsService', () => {
  let service: SessionApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  //TODO: Affichage de la liste des sessions
  it('should display the list of sessions', () => {

  });

  //TODO: Les informations de la session sont correctement affichées
  it('should correctly display session information', () => {

  });

  //TODO: La session est crée
  it('should create a session', () => {

  });

  //TODO: La session est correctement supprimé
  it('should correctly delete a session', () => {

  });

  //TODO: La session est modifiée
  it('should modify a session', () => {

  });
});
