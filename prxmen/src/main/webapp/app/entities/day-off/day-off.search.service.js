(function() {
    'use strict';

    angular
        .module('prxmenApp')
        .factory('DayOffSearch', DayOffSearch);

    DayOffSearch.$inject = ['$resource'];

    function DayOffSearch($resource) {
        var resourceUrl =  'api/_search/day-offs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
