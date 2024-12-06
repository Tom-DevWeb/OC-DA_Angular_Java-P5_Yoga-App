# Front-end Angular

## Prérequis installation

Faire `npm install`

## Démarrer le projet

1. Faire `ng serve` pour démarrer le serveur de développement.
2. Aller sur `http://localhost:4200/`.

## Se connecter sur l'application web

Le compte Admin par default :
- login: yoga@studio.com
- password: test!1234


## Démarrer les tests unitaires et d'intégration

### TU ou TI

`npm run test`

### Test de couverture

npm run test -- --coverage

> [!NOTE]
> Le rapport de couverture est disponible ici :
> `front/coverage/jest/lcov-report/index.html`

### E2E

Lancer test e2e :

`npm run e2e`

Générer un rapport de couverture :

`npm run e2e:coverage`

> [!NOTE]
> Le rapport de couverture est disponible ici :
> `front/coverage/lcov-report/index.html`

> [!WARNING]
> Le rapport de couverture généré par Cypress correspond au dernier fichier .cy.ts démarrer pour un test.
> Il ne lance pas tous les fichiers pour générer le taux de couverture.
> Plus de détails en faisant: `nyc report`

