'use strict';

/* @ngInject */
function Home($rootScope, $uibModal, $state, $q, WorkflowService, PageService, ConfirmationModalService, SessionManagerApi,
              NotificationService, WorkflowsApiClient, config, UserService) {
  this.init = () => {
    PageService.setTitle('Home');
    $rootScope.stateData.dataIsLoaded = true;
    this._isWorkflowLoading = false;
    this.isWorkflowListEmpty = false;
    this.isErrorConnectingToVagrant = false;
    this.icon = '';
    this.info = '';
    this.sort = {
      column: 'updated',
      descending: true
    };

    this.downloadWorkflows();
  };

  this.downloadWorkflows = () => {
    this._isWorkflowLoading = true;
    WorkflowService.downloadWorkflows().then((workflows) => {
      if (workflows && workflows.length === 0) {
        this.isWorkflowListEmpty = true;
      }
      this._isWorkflowLoading = false;
      this.workflows = workflows;
    }, () => {
      this.isErrorConnectingToVagrant = true;
    });
  };

  this.reloadPage = () => {
    window.location.reload();
  };

  this.getTriggerEventBasedOnDescriptionLength = (description) => {
    return description.length > 50 ? 'mouseenter' : 'none';
  };

  this.sortBy = (columnName) => {
    if (this.sort.column === columnName) {
      this.sort.descending = !this.sort.descending;
    } else {
      this.sort.column = columnName;
      this.sort.descending = false;
    }
  };

  this.getCurrentUser = () => {
    return UserService.getSeahorseUser();
  };

  this.isWorkflowOwnedByCurrentUser = (workflow) => {
    return UserService.getSeahorseUser().id == workflow.ownerId;
  };

  this.isWorkflowLoading = () => {
    return this._isWorkflowLoading;
  };

  this.getClass = (columnName) => {
    if (this.sort.column === columnName) {
      let icon = 'glyphicon glyphicon-chevron';
      return this.sort.descending ? icon + '-down' : icon + '-up';
    }
  };

  this.getVersion = () => {
    return config.editorVersion;
  };

  this.getAllWorkflows = () => {
    return WorkflowService.getAllWorkflows();
  };

  this.goToWorkflowEditor = (workflowId) => {
    $state.go('workflows.editor', {id: workflowId});
  };

  this.cloneWorkflow = (workflow) => {
    const modal = $uibModal.open({
      animation: true,
      templateUrl: 'app/common/modals/workflow-clone-modal/workflow-clone-modal.html',
      controller: 'WorkflowCloneModalCtrl as controller',
      backdrop: 'static',
      keyboard: true,
      resolve: {
        workflow: () => angular.copy(workflow)
      }
    });

    modal.result.then((workflowToClone) => {
      WorkflowsApiClient.cloneWorkflow(workflowToClone).then(() => {
        this.downloadWorkflows();
      }, () => {
        NotificationService.showWithParams({
          message: 'There was an error during copying this workflow.',
          title: 'Workflow copy',
          settings: {timeOut: 10000},
          notificationType: 'error'
        });
      });
    });
  };

  this.deleteWorkflow = function (workflow) {
    ConfirmationModalService.showModal({
      message: 'The operation will delete workflow "' + workflow.name + '". Deletion cannot be undone afterwards.'
    }).then(() => {
      WorkflowService.deleteWorkflow(workflow.id);
      SessionManagerApi.deleteSessionById(workflow.id);
    });
  };

  this.deleteSession = (workflowId) => {
    SessionManagerApi.deleteSessionById(workflowId).then(() => {
      this.downloadWorkflows();
    });
  };

  this.displayCreateWorkflowPopup = (event) => {
    event.preventDefault();

    const modal = $uibModal.open({
      animation: true,
      templateUrl: 'app/common/modals/new-workflow-modal/new-workflow-modal.html',
      controller: 'NewWorkflowModalController as controller',
      backdrop: 'static',
      keyboard: true
    });

    modal.result.then((workflowId) => {
      $state.go('workflows.editor', {
        id: workflowId
      });
    });
  };

  this.displayUploadWorkflowPopup = (event) => {
    event.preventDefault();

    const modal = $uibModal.open({
      animation: true,
      templateUrl: 'app/common/modals/upload-workflow-modal/upload-workflow-modal.html',
      controller: 'UploadWorkflowModalController as controller',
      backdrop: 'static',
      keyboard: true
    });

    modal.result.then((workflowId) => {
      $state.go('workflows.editor', {
        'id': workflowId
      });
    });
  };

  this.displayUploadExecutionWorkflowPopup = (event) => {
    event.preventDefault();

    const modal = $uibModal.open({
      animation: false,
      templateUrl: 'app/common/modals/upload-execution-report-modal/upload-execution-report-modal.html',
      controller: 'UploadWorkflowExecutionReportModalController as controller',
      backdrop: 'static',
      keyboard: true
    });

    modal.result.then((reportId) => {
      $state.go('workflows.report', {
        'reportId': reportId
      });
    });
  };

  this.init();
}

exports.function = Home;

exports.inject = function (module) {
  module.controller('Home', Home);
};
