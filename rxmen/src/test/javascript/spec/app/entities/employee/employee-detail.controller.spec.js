'use strict';

describe('Controller Tests', function() {

    describe('Employee Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployee, MockRestWorkDays, MockJob, MockJobHistory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockRestWorkDays = jasmine.createSpy('MockRestWorkDays');
            MockJob = jasmine.createSpy('MockJob');
            MockJobHistory = jasmine.createSpy('MockJobHistory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Employee': MockEmployee,
                'RestWorkDays': MockRestWorkDays,
                'Job': MockJob,
                'JobHistory': MockJobHistory
            };
            createController = function() {
                $injector.get('$controller')("EmployeeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rxmenApp:employeeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
