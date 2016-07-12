'use strict';

describe('Controller Tests', function() {

    describe('Vendor Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockVendor, MockContact, MockLocation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockVendor = jasmine.createSpy('MockVendor');
            MockContact = jasmine.createSpy('MockContact');
            MockLocation = jasmine.createSpy('MockLocation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Vendor': MockVendor,
                'Contact': MockContact,
                'Location': MockLocation
            };
            createController = function() {
                $injector.get('$controller')("VendorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'prxmenApp:vendorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
