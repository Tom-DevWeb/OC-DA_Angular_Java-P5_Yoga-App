/**
 * @type {Cypress.PluginConfig}
 */
 import * as registerCodeCoverageTasks from '@cypress/code-coverage/task.js';

 export default (on, config) => {
   return registerCodeCoverageTasks(on, config);
 };
