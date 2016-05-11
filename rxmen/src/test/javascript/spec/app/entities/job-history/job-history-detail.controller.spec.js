'use strict';

describe('Controller Tests', function() {

    describe('JobHistory Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockJobHistory, MockCustomer, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockJobHistory = jasmine.createSpy('MockJobHistory');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'JobHistory': MockJobHistory,
                'Customer': MockCustomer,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("JobHistoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rxmenApp:jobHistoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
