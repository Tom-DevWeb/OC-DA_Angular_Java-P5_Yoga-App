import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';
import {UserService} from './user.service';
import {MeComponent} from "../components/me/me.component";
import {Router} from "@angular/router";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {SessionService} from "./session.service";
import {By} from "@angular/platform-browser";
import {MatSnackBarModule} from "@angular/material/snack-bar";

describe('UserService', () => {
  let service: UserService;
  let sessionService: SessionService
  let component: MeComponent
  let fixture: ComponentFixture<MeComponent>
  let router: Router;
  let httpTestingController: HttpTestingController

  beforeEach( async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports:[
        HttpClientModule,
        HttpClientTestingModule,
        MatSnackBarModule,
        RouterTestingModule.withRoutes([
          { path: '', redirectTo: '', pathMatch: 'full'}
        ])
      ],
      providers: [UserService, SessionService]
    }).compileComponents()
    service = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService)
    fixture = TestBed.createComponent(MeComponent)
    component = fixture.componentInstance
    router = TestBed.inject(Router);
    httpTestingController = TestBed.inject(HttpTestingController)

    sessionService.sessionInformation = {
      id: 1,
      token: 'mockToken',
      type: 'admin',
      username: 'testuser',
      firstName: 'John',
      lastName: 'Doe',
      admin: false,
    };

    component.user = {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: 'fakepassword',
      admin: false,
      createdAt: new Date(),
      updatedAt: new Date(),
    };
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  //TODO: La suppression de son compte par l'utilisateur
  it('should delete the user account and log out', () => {
    const navigateSpy = jest.spyOn(router, 'navigate');
    const logOutSpy = jest.spyOn(sessionService, 'logOut');

    // Détecte les changements pour initialiser le DOM du composant
    fixture.detectChanges();

    // Trouve le bouton de suppression et simule un clic
    const deleteButton = fixture.debugElement.query(By.css('button[mat-raised-button][color="warn"]'));
    expect(deleteButton).toBeTruthy(); // Vérifie que le bouton est bien rendu

    deleteButton.nativeElement.click(); // Simule le clic utilisateur

    // Capture toutes les requêtes HTTP envoyées
    const requests = httpTestingController.match((req) => req.url === 'api/user/1');
    expect(requests.length).toBe(1); // Vérifie qu'une seule requête DELETE a été envoyée

    // Vérifiez que c'est bien un DELETE
    const deleteRequest = requests[0];
    expect(deleteRequest.request.method).toBe('DELETE');

    // Simulez une réponse de succès pour cette requête
    deleteRequest.flush({});

    // Vérifie que les effets secondaires se sont produits
    expect(logOutSpy).toHaveBeenCalled(); // Vérifie que la déconnexion a été effectuée
    expect(navigateSpy).toHaveBeenCalledWith(['/']);
  });
});
