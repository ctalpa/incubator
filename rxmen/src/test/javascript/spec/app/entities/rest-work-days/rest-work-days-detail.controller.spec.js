'use strict';

describe('Controller Tests', function() {

    describe('RestWorkDays Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRestWorkDays, MockEmployee, MockDayOff;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRestWorkDays = jasmine.createSpy('MockRestWorkDays');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockDayOff = jasmine.createSpy('MockDayOff');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'RestWorkDays': MockRestWorkDays,
                'Employee': MockEmployee,
                'DayOff': MockDayOff
            };
            createController = function() {
                $injector.get('$controller')("RestWorkDaysDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rxmenApp:restWorkDaysUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
