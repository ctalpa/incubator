'use strict';

describe('Controller Tests', function() {

    describe('CompanyVehicles Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCompanyVehicles;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCompanyVehicles = jasmine.createSpy('MockCompanyVehicles');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CompanyVehicles': MockCompanyVehicles
            };
            createController = function() {
                $injector.get('$controller')("CompanyVehiclesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'prxmenApp:companyVehiclesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
