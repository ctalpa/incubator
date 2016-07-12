'use strict';

describe('Controller Tests', function() {

    describe('DayOff Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDayOff, MockRestWorkDays;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDayOff = jasmine.createSpy('MockDayOff');
            MockRestWorkDays = jasmine.createSpy('MockRestWorkDays');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DayOff': MockDayOff,
                'RestWorkDays': MockRestWorkDays
            };
            createController = function() {
                $injector.get('$controller')("DayOffDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'prxmenApp:dayOffUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
