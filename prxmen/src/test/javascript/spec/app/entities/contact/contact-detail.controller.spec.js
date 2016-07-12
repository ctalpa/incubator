'use strict';

describe('Controller Tests', function() {

    describe('Contact Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockContact, MockCustomer, MockVendor;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockContact = jasmine.createSpy('MockContact');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockVendor = jasmine.createSpy('MockVendor');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Contact': MockContact,
                'Customer': MockCustomer,
                'Vendor': MockVendor
            };
            createController = function() {
                $injector.get('$controller')("ContactDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'prxmenApp:contactUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
