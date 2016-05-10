(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .factory('RestWorkDaysSearch', RestWorkDaysSearch);

    RestWorkDaysSearch.$inject = ['$resource'];

    function RestWorkDaysSearch($resource) {
        var resourceUrl =  'api/_search/rest-work-days/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
